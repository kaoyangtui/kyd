package com.pig4cloud.pigx.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.constants.CnirpDisplayColsConstants;
import com.pig4cloud.pigx.constants.CnirpExpConstants;
import com.pig4cloud.pigx.entity.PatentInfoEntity;
import com.pig4cloud.pigx.entity.PatentLogEntity;
import com.pig4cloud.pigx.mapper.PatentInfoMapper;
import com.pig4cloud.pigx.model.request.PatentSearchListReq;
import com.pig4cloud.pigx.model.response.PatentSearchListRes;
import com.pig4cloud.pigx.service.CniprService;
import com.pig4cloud.pigx.service.PatentInfoService;
import com.pig4cloud.pigx.utils.CniprExpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 专利信息表
 *
 * @author zl
 * @date 2025-04-15 13:09:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatentInfoServiceImpl extends ServiceImpl<PatentInfoMapper, PatentInfoEntity> implements PatentInfoService {

    private final CniprService cniprService;

    @Override
    public IPage<PatentSearchListRes> searchList(PatentSearchListReq req) {
        List<String> expResult = new ArrayList<>();

        // 1. 专利名称
        if (StrUtil.isNotBlank(req.getPatentTitle())) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.TITLE, req.getPatentTitle()));
        }

        // 2. 申请号
        if (StrUtil.isNotBlank(req.getAppNumber())) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.APP_NUMBER, req.getAppNumber()));
        }

        // 3. 摘要
        if (StrUtil.isNotBlank(req.getAbs())) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.ABS, req.getAbs()));
        }

        // 4. 专利类型
        if (req.getPatentTypeArray() != null && !req.getPatentTypeArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.PAT_TYPE, req.getPatentTypeArray()));
        }

        // 5. 法律状态
        if (req.getLegalStatusArray() != null && !req.getLegalStatusArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.LEGAL_STATUS, req.getLegalStatusArray()));
        }

        // 6. 技术领域（IPC分类）
        if (req.getIpcArray() != null && !req.getIpcArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.IPC, req.getIpcArray()));
        }

        if (req.getIpcSectionArray() != null && !req.getIpcSectionArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.IPC_SECTION, req.getIpcSectionArray()));
        }

        if (req.getIpcClassArray() != null && !req.getIpcClassArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.IPC_CLASS, req.getIpcClassArray()));
        }

        if (req.getIpcSubClassArray() != null && !req.getIpcSubClassArray().isEmpty()) {
            expResult.add(CniprExpUtils.getExpIn(CnirpExpConstants.IPC_SUB_CLASS, req.getIpcSubClassArray()));
        }

        // 7. 专利申请日（日期段）
        if (req.getApplicationDateRange() != null && req.getApplicationDateRange().length == 2) {
            expResult.add(CniprExpUtils.getExpBetween(CnirpExpConstants.APP_DATE, req.getApplicationDateRange()[0], req.getApplicationDateRange()[1]));
        }

        // 8. 公开（公告）日（日期段）
        if (req.getPublicationDateRange() != null && req.getPublicationDateRange().length == 2) {
            expResult.add(CniprExpUtils.getExpBetween(CnirpExpConstants.PUB_DATE, req.getPublicationDateRange()[0], req.getPublicationDateRange()[1]));
        }

        // 9. 申请人或专利权人
        if (StrUtil.isNotBlank(req.getCurrentApplicantOrOwner())) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.APPLICANT_NAME, req.getCurrentApplicantOrOwner()));
        }

        // 10. 发明人
        if (StrUtil.isNotBlank(req.getInventor())) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.INVENTOR_NAME, req.getInventor()));
        }

        // 11. 高价值发明专利
        if (req.getHighValueFlag() != null) {
            expResult.add(CniprExpUtils.getExpEq(CnirpExpConstants.HIGH_VALUE_FLAG, String.valueOf(req.getHighValueFlag())));
        }

        if (req.getHighValueArray() != null && !req.getHighValueArray().isEmpty()) {
            Map<String, String> map = MapUtil.newHashMap();
            for (String item : req.getHighValueArray()) {
                map.put(item, "1");
            }
            expResult.add(CniprExpUtils.getExpMap(map));
        }

        // 12. 排序类型
        // 根据不同的排序类型，构建相应的排序逻辑
        String order = "+appDate";
        if (req.getSortType() != null) {
            // 可以根据 sortType 来设置排序规则
            // 这里仅为示例，你可以根据需要添加更多的排序逻辑
            switch (req.getSortType()) {
                case 1:
                    // 按申请日倒序
                    order = "-appDate";
                    break;
                case 2:
                    // 按申请人顺序
                    order = "+appDate";
                    break;
                default:
                    break;
            }
        }

        // 生成最终的查询条件表达式
        String exp = CniprExpUtils.getExpAnd(expResult);
        String dbs = "FMZL,FMSQ,SYXX,WGZL,USPATENT,GBPATENT,FRPATENT,DEPATENT,CHPATENT,JPPATENT,RUPATENT,KRPATENT,EPPATENT,WOPATENT";
        //检索类型，默认值：2 （按字检索）其它值含义见附录：https://open.cnipr.com/oauth/doc/appendix#.option
        int option = 2;
        String displayCols = CnirpDisplayColsConstants.ALL_FIELDS;
        boolean highLight = false;
        boolean isDbAgg = false;
        int from = req.getFrom();
        int size = req.getSize();
        Page<PatentLogEntity> patentLogEntityPage = cniprService.page(exp, dbs, option, order, from, size, displayCols, highLight, isDbAgg);
        IPage<PatentSearchListRes> resPage = patentLogEntityPage.convert(item -> {
            PatentInfoEntity patentInfo = JSONUtil.toBean(item.getResponseBody(), PatentInfoEntity.class);
            PatentSearchListRes res = new PatentSearchListRes();
            res.setPatentName(patentInfo.getTitle());
            res.setPatentType(patentInfo.getPatenteType());
            res.setLegalStatus(patentInfo.getLegalStatus());
            res.setApplicationNumber(patentInfo.getAppNumber());
            res.setApplicationDate(patentInfo.getAppDate());
            res.setPublicationNumber(patentInfo.getPubNumber());
            res.setPublicationDate(patentInfo.getPubDate());
            res.setGrantDate(patentInfo.getGrantDate());
            res.setIpc(patentInfo.getIpc());
            res.setNationalEconomy(patentInfo.getNec());
            res.setCurrentApplicant(patentInfo.getApplicantInfo());
            res.setCurrentOwner(patentInfo.getPatentee());
            res.setInventor(patentInfo.getInventorName());
            res.setAgency(patentInfo.getAgencyName());
            res.setAgent(patentInfo.getAgentName());
            return res;
        });

        return resPage;
    }

}