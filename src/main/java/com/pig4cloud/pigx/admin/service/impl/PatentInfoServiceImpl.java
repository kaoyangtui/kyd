package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.CnirpDisplayColsConstants;
import com.pig4cloud.pigx.admin.constants.CnirpExpConstants;
import com.pig4cloud.pigx.admin.constants.DiPatentConstants;
import com.pig4cloud.pigx.admin.constants.DiPatentLegalStatusEnum;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.dto.patent.cnipr.Legal;
import com.pig4cloud.pigx.admin.entity.PatentDetailEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.es.PatentEsEntity;
import com.pig4cloud.pigx.admin.es.mapper.PatentEsMapper;
import com.pig4cloud.pigx.admin.mapper.PatentInfoMapper;
import com.pig4cloud.pigx.admin.service.YtService;
import com.pig4cloud.pigx.admin.service.DataScopeService;
import com.pig4cloud.pigx.admin.service.PatentDetailService;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.utils.CniprExpUtils;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.kernel.EsWrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final YtService ytService;
    private final DataScopeService dataScopeService;
    private final PatentEsMapper patentEsMapper;
    private final PatentDetailService patentDetailService;

    @Override
    public IPage<PatentInfoResponse> pageResult(Page page, PatentPageRequest request) {
        LambdaQueryWrapper<PatentInfoEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            List<Long> ids = request.getIds();
            wrapper.in(PatentInfoEntity::getId, ids)
                    .last("ORDER BY FIELD(id, " + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")");
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(PatentInfoEntity::getTitle, request.getKeyword())
                        .or()
                        .like(PatentInfoEntity::getAppNumber, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getPatType()), PatentInfoEntity::getPatType, request.getPatType());
            wrapper.eq(StrUtil.isNotBlank(request.getLegalStatus()), PatentInfoEntity::getLegalStatus, request.getLegalStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getApplicantName()), PatentInfoEntity::getApplicantName, request.getApplicantName());
            wrapper.eq(StrUtil.isNotBlank(request.getInventorName()), PatentInfoEntity::getInventorName, request.getInventorName());
            //wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PatentInfoEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginAppDate()), PatentInfoEntity::getAppDate, request.getBeginAppDate());
            wrapper.le(StrUtil.isNotBlank(request.getEndAppDate()), PatentInfoEntity::getAppDate, request.getEndAppDate());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginPubDate()), PatentInfoEntity::getPubDate, request.getBeginPubDate());
            wrapper.le(StrUtil.isNotBlank(request.getEndPubDate()), PatentInfoEntity::getPubDate, request.getEndPubDate());
            wrapper.eq(StrUtil.isNotBlank(request.getAgencyName()), PatentInfoEntity::getAgencyName, request.getAgencyName());
            wrapper.eq(StrUtil.isNotBlank(request.getLeaderCode()), PatentInfoEntity::getLeaderCode, request.getLeaderCode());
            wrapper.eq(StrUtil.isNotBlank(request.getMergeFlag()), PatentInfoEntity::getMergeFlag, request.getMergeFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getTransferFlag()), PatentInfoEntity::getTransferFlag, request.getTransferFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getClaimFlag()), PatentInfoEntity::getTransferFlag, request.getClaimFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getShelfFlag()), PatentInfoEntity::getShelfFlag, request.getShelfFlag());
            //数据权限
            DataScope dataScope = DataScope.of();
            if (!dataScopeService.calcScope(dataScope)) {
                List<Long> deptIds = dataScope.getDeptList();
                String deptIn = CollUtil.join(deptIds, ",");
                String name = dataScope.getUsername();
                // 1.无数据权限限制，则直接返回 0 条数据
                if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(name)) {
                    return null;
                }
                // 2.如果为本人权限 + 部门权限控制
                if (StrUtil.isNotBlank(name) && CollUtil.isNotEmpty(deptIds)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and (name = {0} or dept_id in (" + deptIn + ")))", name);
                }
                // 3. 如果为本人
                else if (StrUtil.isNotBlank(name)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and name = {0})", name);
                }
                // 4.部门权限控制
                else {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and dept_id in (" + deptIn + "))");
                }
            }
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<PatentInfoEntity> entityPage = this.page(page, wrapper);

        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, PatentInfoResponse.class));
    }

    @Override
    public IPage<PatentSearchResponse> searchPatent(Page page, PatentSearchRequest request) {
        int offset = ((int) page.getCurrent() - 1) * (int) page.getSize();
        // 默认排序
        String orderBy = "ORDER BY t2.shelf_time DESC";
        if (CollUtil.isNotEmpty(page.orders())) {
            List<String> items = new ArrayList<>();
            for (Object orderObj : page.orders()) {
                OrderItem order = (OrderItem) orderObj;
                String column = order.getColumn();
                if (StrUtil.isNotBlank(column)) {
                    // 只拼 t1. 前缀，且参数只传实际字段名（如 grant_date）
                    items.add("t1." + column + (order.isAsc() ? " ASC" : " DESC"));
                }
            }
            if (!items.isEmpty()) {
                orderBy = "ORDER BY " + String.join(", ", items);
            }
        }

        List<PatentSearchResponse> records = baseMapper.searchPatent(
                request.getKeyword(),
                offset,
                (int) page.getSize(),
                orderBy
        );
        int total = baseMapper.countSearch(request.getKeyword());
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }


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
        log.info("exp:{}", exp);
        String dbs = "FMZL,FMSQ,SYXX,WGZL,USPATENT,GBPATENT,FRPATENT,DEPATENT,CHPATENT,JPPATENT,RUPATENT,KRPATENT,EPPATENT,WOPATENT";
        //检索类型，默认值：2 （按字检索）其它值含义见附录：https://open.cnipr.com/oauth/doc/appendix#.option
        int option = 2;
        String displayCols = CnirpDisplayColsConstants.ALL_FIELDS;
        boolean highLight = false;
        boolean isDbAgg = false;
        int from = req.getFrom();
        int size = req.getSize();
        Page<PatentLogEntity> patentLogEntityPage = ytService.page(exp, dbs, option, order, from, size, displayCols, highLight, isDbAgg);
        IPage<PatentSearchListRes> resPage = patentLogEntityPage.convert(item -> {
            PatentInfoEntity patentInfo = JSONUtil.toBean(item.getResponseBody(), PatentInfoEntity.class);
            PatentSearchListRes res = new PatentSearchListRes();
            res.setPatentName(patentInfo.getTitle());
            res.setPatentType(patentInfo.getPatenteType());
            //res.setLegalStatus(patentInfo.getLegalStatus());
            res.setApplicationNumber(patentInfo.getAppNumber());
            res.setApplicationDate(patentInfo.getAppDate());
            res.setPublicationNumber(patentInfo.getPubNumber());
            res.setPublicationDate(patentInfo.getPubDate());
            res.setGrantDate(patentInfo.getGrantDate());
            res.setIpc(patentInfo.getIpc());
            res.setNationalEconomy(patentInfo.getNec());
            //res.setCurrentApplicant(patentInfo.getApplicantInfo());
            res.setCurrentOwner(patentInfo.getPatentee());
            res.setInventor(patentInfo.getInventorName());
            res.setAgency(patentInfo.getAgencyName());
            res.setAgent(patentInfo.getAgentName());
            return res;
        });

        return resPage;
    }

    @Override
    public void create(PatentInfoEntity patentInfo, String message) {
        patentInfo.setId(null);
        patentInfo.setAppNumber(CodeUtils.getFirstCode(patentInfo.getAppNumber()));
        patentInfo.setPubNumber(CodeUtils.getFirstCode(patentInfo.getPubNumber()));
        patentInfo.setApplicantName(CodeUtils.formatCodes(patentInfo.getApplicantName()));
        patentInfo.setApplicantType(CodeUtils.formatCodes(patentInfo.getApplicantType()));
        patentInfo.setInventorName(CodeUtils.formatCodes(patentInfo.getInventorName()));
        patentInfo.setPatentee(CodeUtils.formatCodes(patentInfo.getPatentee()));
        patentInfo.setNec(CodeUtils.formatCodes(patentInfo.getNec()));
        patentInfo.setIpc(CodeUtils.formatCodes(patentInfo.getIpc()));
        patentInfo.setIpcSection(CodeUtils.formatCodes(patentInfo.getIpcSection()));
        patentInfo.setIpcClass(CodeUtils.formatCodes(patentInfo.getIpcClass()));
        patentInfo.setIpcSubClass(CodeUtils.formatCodes(patentInfo.getIpcSubClass()));
        patentInfo.setIpcGroup(CodeUtils.formatCodes(patentInfo.getIpcGroup()));
        patentInfo.setIpcSubGroup(CodeUtils.formatCodes(patentInfo.getIpcSubGroup()));
        patentInfo.setAgentName(CodeUtils.formatCodes(patentInfo.getAgentName()));
        patentInfo.setPatentWords(CodeUtils.formatCodes(patentInfo.getPatentWords()));
        patentInfo.setTitleKey(CodeUtils.formatCodes(patentInfo.getTitleKey()));
        patentInfo.setClKey(CodeUtils.formatCodes(patentInfo.getClKey()));
        patentInfo.setBgKey(CodeUtils.formatCodes(patentInfo.getBgKey()));
        patentInfo.setHistoryPatentee(CodeUtils.formatCodes(patentInfo.getHistoryPatentee()));
        patentInfo.setPatenteType(CodeUtils.formatCodes(patentInfo.getPatenteType()));
        patentInfo.setTransferFlag(calcTransferFlag(patentInfo.getApplicantName(), patentInfo.getPatentee()));
        JSONObject msgJo = JSONUtil.parseObj(message);
        //法律信
        String legalListStr = msgJo.getStr("legalList");
        if (StrUtil.isNotBlank(legalListStr)) {
            List<Legal> legalList = JSONUtil.toList(legalListStr, Legal.class);
            for (Legal legal : legalList) {
                String prsDate = legal.getPrsDate();
                String prsCode = legal.getPrsCode();
                if (DiPatentConstants.EXAMINATION_STATUS.contains(prsCode)) {
                    patentInfo.setExaminationDate(prsDate);
                }
                if (DiPatentConstants.UNAUTHORIZED_STATUS.contains(prsCode)) {
                    patentInfo.setUnAuthorizedDate(prsDate);
                }
                if (DiPatentConstants.UNEFFECTIVE_STATUS.contains(prsCode)) {
                    patentInfo.setUnEffectiveDate(prsDate);
                }
            }
        }
        //公开中逻辑：公开=true and 实审数量=false and 授权数量=false and 未授权数量=false and 失效数量=false
        if (StrUtil.isNotBlank(patentInfo.getPubDate())
                && StrUtil.isBlank(patentInfo.getExaminationDate())
                && StrUtil.isBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.PUBLICATION.getDesc());
        }
        //实审逻辑：实审=true and 授权数量=false and 未授权数量=false and 失效数量=false
        else if (StrUtil.isNotBlank(patentInfo.getExaminationDate())
                && StrUtil.isBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.EXAMINATION.getDesc());
        }
        //未授权逻辑：未授权数量=true
        else if (StrUtil.isNotBlank(patentInfo.getUnAuthorizedDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.UNAUTHORIZED.getDesc());
        }
        //有效逻辑：授权数量=true and 未授权数量=false and 失效数量=false
        else if (StrUtil.isNotBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.EFFECTIVE.getDesc());
        }
        //无效逻辑：失效数量=true
        else if (StrUtil.isNotBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.UNEFFECTIVE.getDesc());
        }
        //法律状态补充逻辑
        //10	有效专利
        //20	失效专利
        //21	专利权届满的专利
        //22	在审超期
        //30	在审专利
        if (patentInfo.getStatusCode() != null) {
            switch (patentInfo.getStatusCode()) {
                case "10":
                    patentInfo.setLegalStatus(DiPatentLegalStatusEnum.EFFECTIVE.getDesc());
                    break;
                case "20":
                    patentInfo.setLegalStatus(DiPatentLegalStatusEnum.UNEFFECTIVE.getDesc());
                    break;
            }
        }

        PatentInfoEntity oldPatentInfo = this.lambdaQuery()
                .eq(PatentInfoEntity::getPid, patentInfo.getPid())
                .one();
        if (oldPatentInfo != null) {
            // 全局忽略源对象中为 null 的属性，目标对象原值不动
            BeanUtil.copyProperties(
                    patentInfo,
                    oldPatentInfo,
                    CopyOptions.create().setIgnoreNullValue(true)
            );
            this.updateById(oldPatentInfo);
            log.info("更新成功: {}", oldPatentInfo);
        } else {
            this.save(patentInfo);
            log.info("保存成功: {}", patentInfo);
        }
        //处理专利申请号合并
        this.lambdaUpdate()
                .eq(PatentInfoEntity::getAppNumber, patentInfo.getAppNumber())
                .eq(PatentInfoEntity::getMergeFlag, 1)
                .ne(PatentInfoEntity::getPid, patentInfo.getPid())
                .set(PatentInfoEntity::getMergeFlag, 0)
                .update();
    }

    @Override
    public Page<PatentEsPageResponse> esPage(PatentEsPageRequest req) {
        LambdaEsQueryWrapper<PatentEsEntity> wrapper = EsWrappers.lambdaQuery(PatentEsEntity.class);

        // 关键词，支持名称、申请号模糊
        if (StrUtil.isNotBlank(req.getKeyword())) {
            wrapper.must(qw -> qw
                    .should(sq -> sq.match(PatentEsEntity::getTitle, req.getKeyword()))
                    .should(sq -> sq.match(PatentEsEntity::getAppNumber, req.getKeyword()))
            );
        }

        // 专利类型
        if (req.getPatentTypes() != null && !req.getPatentTypes().isEmpty()) {
            wrapper.in(PatentEsEntity::getPatType, req.getPatentTypes());
        }

        // 法律状态
        if (req.getLegalStatuses() != null && !req.getLegalStatuses().isEmpty()) {
            wrapper.in(PatentEsEntity::getLegalStatus, req.getLegalStatuses());
        }

        // 申请人
        if (StrUtil.isNotBlank(req.getApplicantName())) {
            wrapper.match(PatentEsEntity::getApplicantName, req.getApplicantName());
        }

        // 发明人
        if (StrUtil.isNotBlank(req.getInventorName())) {
            wrapper.match(PatentEsEntity::getInventorName, req.getInventorName());
        }

        // IPC 分类
        if (req.getIpc() != null && !req.getIpc().isEmpty()) {
            wrapper.in(PatentEsEntity::getIpc, req.getIpc());
        }

        // 国民经济行业
        if (req.getIndustries() != null && !req.getIndustries().isEmpty()) {
            wrapper.in(PatentEsEntity::getNec, req.getIndustries());
        }

        // 国家战略性新兴产业
        if (req.getStrategicIndustries() != null && !req.getStrategicIndustries().isEmpty()) {
            wrapper.in(PatentEsEntity::getStrategicEmergingIndustryFlag, req.getStrategicIndustries());
        }

        // 江苏省高新技术产业
        //if (req.getHighTechIndustries() != null && !req.getHighTechIndustries().isEmpty()) {
        //    wrapper.in(PatentEsEntity::getHighTechIndustry, req.getHighTechIndustries());
        //}

        // 知识产权密集型技术
        //if (req.getIpTechTypes() != null && !req.getIpTechTypes().isEmpty()) {
        //    wrapper.in(PatentEsEntity::getIpTechType, req.getIpTechTypes());
        //}

        // 区域
        if (StrUtil.isNotBlank(req.getRegion())) {
            wrapper.eq(PatentEsEntity::getAddrCounty, req.getRegion());
        }

        // 区域级别（可以拓展实现：如 regionLevel=“市” 则 eq city）
        if (StrUtil.isNotBlank(req.getRegionLevel())) {
            switch (req.getRegionLevel()) {
                case "省" -> wrapper.isNotNull(PatentEsEntity::getAddrProvince);
                case "市" -> wrapper.isNotNull(PatentEsEntity::getAddrCity);
                case "区" -> wrapper.isNotNull(PatentEsEntity::getAddrCounty);
                default -> {
                }
            }
        }

        // 申请日范围
        if (req.getAppDateStart() != null || req.getAppDateEnd() != null) {
            String from = req.getAppDateStart() != null ? req.getAppDateStart().toString() : null;
            String to = req.getAppDateEnd() != null ? req.getAppDateEnd().toString() : null;
            wrapper.between(PatentEsEntity::getAppDate, from, to);
        }

        // 公告日范围
        if (req.getPubDateStart() != null || req.getPubDateEnd() != null) {
            String from = req.getPubDateStart() != null ? req.getPubDateStart().toString() : null;
            String to = req.getPubDateEnd() != null ? req.getPubDateEnd().toString() : null;
            wrapper.between(PatentEsEntity::getPubDate, from, to);
        }

        // 排序
        String orderField = req.getOrderField();
        String orderType = req.getOrderType();
        if (StrUtil.isNotBlank(orderField)) {
            // 允许的排序字段：appDate, pubDate, viewCount, grantDate, ...
            boolean asc = "asc".equalsIgnoreCase(orderType);
            if (asc) {
                switch (orderField) {
                    case "appDate" -> wrapper.orderByAsc(PatentEsEntity::getAppDate);
                    case "pubDate" -> wrapper.orderByAsc(PatentEsEntity::getPubDate);
                    case "viewCount" -> wrapper.orderByAsc(PatentEsEntity::getViewCount);
                    default -> wrapper.orderByAsc(PatentEsEntity::getAppDate);
                }
            } else {
                switch (orderField) {
                    case "appDate" -> wrapper.orderByDesc(PatentEsEntity::getAppDate);
                    case "pubDate" -> wrapper.orderByDesc(PatentEsEntity::getPubDate);
                    case "viewCount" -> wrapper.orderByDesc(PatentEsEntity::getViewCount);
                    default -> wrapper.orderByDesc(PatentEsEntity::getAppDate);
                }
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(PatentEsEntity::getAppDate);
        }

        // 分页查询
        int pageNo = (int) (req.getCurrent() > 0 ? req.getCurrent() : 1);
        int pageSize = (int) (req.getSize() > 0 ? req.getSize() : 10);

        EsPageInfo<PatentEsEntity> esPage = patentEsMapper.pageQuery(wrapper, pageNo, pageSize);

        // 转换返回
        Page<PatentEsPageResponse> result = new Page<>(pageNo, pageSize, esPage.getTotal());
        result.setRecords(new ArrayList<>());

        if (esPage.getList() != null && !esPage.getList().isEmpty()) {
            for (PatentEsEntity entity : esPage.getList()) {
                PatentEsPageResponse resp = new PatentEsPageResponse();
                BeanUtils.copyProperties(entity, resp);
                result.getRecords().add(resp);
            }
        }
        return result;

    }

    private String calcTransferFlag(String applicantName, String patentee) {
        List<String> applicants = StrUtil.splitTrim(applicantName, ";");
        List<String> patentees = StrUtil.splitTrim(patentee, ";");
        // 没有任何申请人在专利权人里时为转移（返回 "1"）
        return CollUtil.isNotEmpty(applicants) && CollUtil.isNotEmpty(patentees)
                && !CollUtil.containsAny(patentees, applicants)
                ? "1" : "0";
    }

    @Override
    public PatentDetailResponse getDetailByPid(String pid) {
        PatentInfoEntity info = this.lambdaQuery()
                .eq(PatentInfoEntity::getPid, pid)
                .one();
        PatentDetailEntity detail = patentDetailService.lambdaQuery()
                .eq(PatentDetailEntity::getPid, pid)
                .one();

        PatentDetailResponse resp = new PatentDetailResponse();
        if (info != null) {
            BeanUtil.copyProperties(info, resp);
        }
        if (detail != null) {
            BeanUtil.copyProperties(detail, resp);
        }
        this.lambdaUpdate()
                .eq(PatentInfoEntity::getPid, pid)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return resp;
    }
}