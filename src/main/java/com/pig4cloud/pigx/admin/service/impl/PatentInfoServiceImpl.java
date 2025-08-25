package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.*;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.dto.patent.cnipr.Legal;
import com.pig4cloud.pigx.admin.entity.PatentDetailCacheEntity;
import com.pig4cloud.pigx.admin.entity.PatentDetailEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.mapper.PatentInfoMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.CniprExpUtils;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final PatentDetailService patentDetailService;
    private final PatentDetailCacheService patentDetailCacheService;
    private final FileService fileService;

    @Override
    public IPage<PatentInfoResponse> pageResult(Page page, PatentPageRequest request) {
        LambdaQueryWrapper<PatentInfoEntity> wrapper = buildWrapper(request);

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(PatentInfoEntity::getPubDate);
        }

        IPage<PatentInfoEntity> entityPage = this.page(page, wrapper);
        if (entityPage == null || CollUtil.isEmpty(entityPage.getRecords())) {
            return null;
        }

        List<String> pidList = entityPage.getRecords().stream()
                .map(PatentInfoEntity::getPid)
                .distinct()
                .toList();

        Map<String, PatentDetailCacheEntity> cacheMap = patentDetailCacheService.lambdaQuery()
                .in(PatentDetailCacheEntity::getPid, pidList)
                .list()
                .stream()
                .collect(Collectors.toMap(PatentDetailCacheEntity::getPid, e -> e, (a, b) -> a));

        return entityPage.convert(entity -> {
            PatentInfoResponse response = BeanUtil.copyProperties(entity, PatentInfoResponse.class);
            PatentDetailCacheEntity detailCacheEntity = cacheMap.get(entity.getPid());
            if (detailCacheEntity != null) {
                response.setDraws(detailCacheEntity.getDraws());
            }
            PatentTypeEnum patentTypeEnum = PatentTypeEnum.getByCode(entity.getPatType());
            if (patentTypeEnum != null) {
                response.setPatTypeName(patentTypeEnum.getDescription());
            } else {
                response.setPatTypeName("未知");
            }
            return response;
        });
    }


    @Override
    public List<PatentTypeSummaryVO> patentTypeSummary(PatentPageRequest request) {
        LambdaQueryWrapper<PatentInfoEntity> wrapper = buildWrapper(request);
        wrapper.isNotNull(PatentInfoEntity::getPatType);
        List<PatentTypeSummaryVO> result = baseMapper.selectGroupSum(wrapper);
        return result.stream()
                .map(entity -> {
                    PatentTypeEnum patentTypeEnum = PatentTypeEnum.getByCode(entity.getPatType());
                    if (patentTypeEnum != null) {
                        entity.setPatTypeName(patentTypeEnum.getDescription());
                    } else {
                        entity.setPatTypeName("未知");
                    }
                    return entity;
                })
                .toList();
    }

    private LambdaQueryWrapper<PatentInfoEntity> buildWrapper(PatentPageRequest request) {
        LambdaQueryWrapper<PatentInfoEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            List<Long> ids = request.getIds();
            wrapper.in(PatentInfoEntity::getId, ids)
                    .last("ORDER BY FIELD(id, " + ids.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")) + ")");
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(PatentInfoEntity::getTitle, request.getKeyword())
                        .or().eq(PatentInfoEntity::getAppNumber, request.getKeyword())
                        .or().eq(PatentInfoEntity::getPubNumber, request.getKeyword())
                        .or().like(PatentInfoEntity::getPatentWords, request.getKeyword())
                );
            }
            wrapper.in(CollUtil.isNotEmpty(request.getPatType()), PatentInfoEntity::getPatType, request.getPatType());
            wrapper.eq(StrUtil.isNotBlank(request.getLegalStatus()), PatentInfoEntity::getLegalStatus, request.getLegalStatus());
            wrapper.like(StrUtil.isNotBlank(request.getApplicantName()), PatentInfoEntity::getApplicantName, request.getApplicantName());
            wrapper.like(StrUtil.isNotBlank(request.getInventorName()), PatentInfoEntity::getInventorName, request.getInventorName());
            // ===== AppDate =====
            String beginAppDate = formatDotDate(request.getBeginAppDate());
            String endAppDate = formatDotDate(request.getEndAppDate());
            wrapper.ge(StrUtil.isNotBlank(beginAppDate), PatentInfoEntity::getAppDate, beginAppDate);
            wrapper.le(StrUtil.isNotBlank(endAppDate), PatentInfoEntity::getAppDate, endAppDate);
            // ===== PubDate =====
            String beginPubDate = formatDotDate(request.getBeginPubDate());
            String endPubDate = formatDotDate(request.getEndPubDate());
            wrapper.ge(StrUtil.isNotBlank(beginPubDate), PatentInfoEntity::getPubDate, beginPubDate);
            wrapper.le(StrUtil.isNotBlank(endPubDate), PatentInfoEntity::getPubDate, endPubDate);

            wrapper.eq(StrUtil.isNotBlank(request.getAgencyName()), PatentInfoEntity::getAgencyName, request.getAgencyName());
            wrapper.eq(StrUtil.isNotBlank(request.getLeaderCode()), PatentInfoEntity::getLeaderCode, request.getLeaderCode());
            wrapper.eq(StrUtil.isNotBlank(request.getMergeFlag()), PatentInfoEntity::getMergeFlag, request.getMergeFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getTransferFlag()), PatentInfoEntity::getTransferFlag, request.getTransferFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getClaimFlag()), PatentInfoEntity::getTransferFlag, request.getClaimFlag());
            wrapper.eq(StrUtil.isNotBlank(request.getShelfFlag()), PatentInfoEntity::getShelfFlag, request.getShelfFlag());
            List<Long> deptIds = CollUtil.newArrayList();
            String name = "";
            // 数据权限
            DataScope dataScope = DataScope.of();
            if (!dataScopeService.calcScope(dataScope)) {
                deptIds = dataScope.getDeptList();
                name = dataScope.getUsername();

                if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(name)) {
                    log.warn("用户 {} 无数据权限", name);
                    wrapper.apply("1=0"); // 直接返回空结果
                    return wrapper;
                }
            }

            if (StrUtil.isNotBlank(request.getDeptId())) {
                if (deptIds.contains(Long.parseLong(request.getDeptId()))) {
                    deptIds = CollUtil.newArrayList(Long.parseLong(request.getDeptId()));
                } else {
                    log.warn("用户 {} 无权查看部门 {} 的专利信息", name, request.getDeptId());
                    wrapper.apply("1=0");
                    return wrapper;
                }
            }

            if (CollUtil.isNotEmpty(deptIds) || StrUtil.isNotBlank(name)) {
                String deptIn = CollUtil.isNotEmpty(deptIds)
                        ? deptIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                        : "";
                if (StrUtil.isNotBlank(name) && CollUtil.isNotEmpty(deptIds)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and (name = {0} or dept_id in (" + deptIn + ")))", name);
                } else if (StrUtil.isNotBlank(name)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and name = {0})", name);
                } else {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and dept_id in (" + deptIn + "))");
                }
            }
        }
        return wrapper;
    }

    private String formatDotDate(LocalDate date) {
        return date != null ? DateTimeFormatter.ofPattern("yyyy.MM.dd").format(date) : null;
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
        StringBuilder whereSql = new StringBuilder();
        whereSql.append("t1.del_flag = 0 AND t2.shelf_status = 1 ");

        if (StrUtil.isNotBlank(request.getKeyword())) {
            whereSql.append("AND MATCH(app_number, pub_number, inventor_name, patent_words, title_key, cl_key, bg_key) ");
            whereSql.append("AGAINST('").append(request.getKeyword()).append("' IN NATURAL LANGUAGE MODE) ");
        }
        if (CollUtil.isNotEmpty(request.getTechArea())) {
            String regex = "(^|;)(" + CollUtil.join(request.getTechArea(), "|") + ")";
            whereSql.append("AND t1.nec REGEXP '").append(regex).append("' ");
        }
        if (CollUtil.isNotEmpty(request.getCooperationMode())) {
            String inStr = request.getCooperationMode().stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(","));
            whereSql.append("AND t2.cooperation_mode IN (").append(inStr).append(") ");
        }
        if (StrUtil.isNotBlank(request.getInventorCode())) {
            whereSql.append(StrUtil.format("AND EXISTS (SELECT 0 FROM t_patent_inventor WHERE pid = t1.pid AND code = '{}') ", request.getInventorCode()));
        }
        if (CollUtil.isNotEmpty(request.getPatType())) {
            String inStr = request.getPatType().stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(","));
            whereSql.append("AND t1.pat_type IN (").append(inStr).append(") ");
        }

        List<PatentSearchResponse> records = baseMapper.searchPatent(
                whereSql.toString(),
                offset,
                (int) page.getSize(),
                orderBy
        );

        int total = baseMapper.countSearch(whereSql.toString());
        records.forEach(patent -> {
            if (StrUtil.isNotBlank(patent.getPatType()) && NumberUtil.isNumber(patent.getPatType())) {
                PatentTypeEnum typeEnum = PatentTypeEnum.getByCode(patent.getPatType());
                if (typeEnum != null) {
                    patent.setPatTypeName(typeEnum.getDescription());
                } else {
                    patent.setPatTypeName("未知");
                }
            }
        });
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
                case "21":
                case "22":
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
            //设置默认值
            patentInfo.setMergeFlag("1");
            patentInfo.setClaimFlag("0");
            patentInfo.setShelfFlag("0");
            patentInfo.setViewCount(0L);
            this.save(patentInfo);
            log.info("保存成功: {}", patentInfo);
            //处理专利申请号合并
            this.lambdaUpdate()
                    .eq(PatentInfoEntity::getAppNumber, patentInfo.getAppNumber())
                    .ne(PatentInfoEntity::getPid, patentInfo.getPid())
                    .set(PatentInfoEntity::getMergeFlag, "0")
                    .update();
        }
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
        PatentInfoEntity info = this.lambdaQuery().eq(PatentInfoEntity::getPid, pid).one();
        PatentDetailEntity detail = patentDetailService.lambdaQuery().eq(PatentDetailEntity::getPid, pid).one();
        PatentDetailCacheEntity cache = patentDetailCacheService.lambdaQuery().eq(PatentDetailCacheEntity::getPid, pid).one();

        PatentDetailResponse resp = new PatentDetailResponse();
        if (info != null) {
            BeanUtil.copyProperties(info, resp, CopyOptions.create().ignoreError());
        }
        if (detail != null) {
            BeanUtil.copyProperties(detail, resp, CopyOptions.create().ignoreError());
        }

        // 新建或补全缓存
        if (cache == null) {
            cache = new PatentDetailCacheEntity();
            cache.setPid(pid);
            String absUrl = ytService.absUrl(pid);
            if (StrUtil.isNotBlank(absUrl)) {
                cache.setDraws(fileService.uploadFileByUrl(absUrl, "abstract", FileGroupTypeEnum.IMAGE));
            }
            cache.setTenantId(1L);
            cache.setStatus(0);
            patentDetailCacheService.save(cache);
        }
        resp.setCover(cache.getDraws());
        resp.setId(info.getId());

        this.lambdaUpdate()
                .eq(PatentInfoEntity::getPid, pid)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return resp;
    }

    @Override
    public PatentDetailResponse getDetailImgByPid(String pid) {
        PatentDetailEntity detail = patentDetailService.lambdaQuery().eq(PatentDetailEntity::getPid, pid).one();
        PatentDetailCacheEntity cache = patentDetailCacheService.lambdaQuery().eq(PatentDetailCacheEntity::getPid, pid).one();
        PatentDetailResponse resp = new PatentDetailResponse();

        // 新建或补全缓存
        if (cache == null) {
            cache = new PatentDetailCacheEntity();
            cache.setPid(pid);
            String absUrl = ytService.absUrl(pid);
            if (StrUtil.isNotBlank(absUrl)) {
                cache.setDraws(fileService.uploadFileByUrl(absUrl, PatentFileTypeEnum.ABSTRACT.getCode(), FileGroupTypeEnum.IMAGE));
            }
            cache.setDrawsPic(buildBatchPicUrls(pid, detail.getIncPic(), PatentFileTypeEnum.SPECIFICATION.getCode(), FileGroupTypeEnum.IMAGE, ytService));
            cache.setTifDistributePath(buildBatchPicUrls(pid, detail.getDesignPic(), PatentFileTypeEnum.DESIGN.getCode(), FileGroupTypeEnum.IMAGE, ytService));
            //cache.setPdf(fileService.uploadFileByUrl(ytService.pdfUrl(pid), "pdf", FileGroupTypeEnum.FILE));
            cache.setTenantId(1L);
            cache.setStatus(1);
            patentDetailCacheService.save(cache);
        } else {
            if (cache.getStatus() != 1) {
                cache.setDrawsPic(buildBatchPicUrls(pid, detail.getIncPic(), PatentFileTypeEnum.SPECIFICATION.getCode(), FileGroupTypeEnum.IMAGE, ytService));
                cache.setTifDistributePath(buildBatchPicUrls(pid, detail.getDesignPic(), PatentFileTypeEnum.DESIGN.getCode(), FileGroupTypeEnum.IMAGE, ytService));
                //cache.setPdf(fileService.uploadFileByUrl(ytService.pdfUrl(pid), "pdf", FileGroupTypeEnum.FILE));
                cache.setStatus(1);
                patentDetailCacheService.updateById(cache);
            }
        }

        resp.setDrawsPic(StrUtil.split(cache.getDrawsPic(), ";"));
        resp.setTifDistributePath(StrUtil.split(cache.getTifDistributePath(), ";"));
        return resp;
    }

    @Override
    public String getDetailPdfByPid(String pid) {
        PatentDetailCacheEntity cache = patentDetailCacheService.lambdaQuery().eq(PatentDetailCacheEntity::getPid, pid).one();

        // 新建或补全缓存
        if (cache == null) {
            cache = new PatentDetailCacheEntity();
            cache.setPid(pid);
            String absUrl = ytService.absUrl(pid);
            if (StrUtil.isNotBlank(absUrl)) {
                cache.setDraws(fileService.uploadFileByUrl(absUrl, PatentFileTypeEnum.ABSTRACT.getCode(), FileGroupTypeEnum.IMAGE));
            }
            cache.setPdf(fileService.uploadFileByUrl(ytService.pdfUrl(pid), PatentFileTypeEnum.PDF.getCode(), FileGroupTypeEnum.FILE));
            cache.setTenantId(1L);
            cache.setStatus(0);
            patentDetailCacheService.save(cache);
        } else {
            if (cache.getStatus() != 1) {
                cache.setPdf(fileService.uploadFileByUrl(ytService.pdfUrl(pid), PatentFileTypeEnum.PDF.getCode(), FileGroupTypeEnum.FILE));
                cache.setStatus(1);
                patentDetailCacheService.updateById(cache);
            }
        }
        return cache.getPdf();
    }

    private String buildBatchPicUrls(String pid, String picJson, String group, FileGroupTypeEnum type, YtService ytService) {
        if (StrUtil.isBlank(picJson)) {
            return null;
        }
        List<String> picList = JSONUtil.toList(JSONUtil.parseArray(picJson), String.class);
        if (CollUtil.isEmpty(picList)) {
            return null;
        }
        List<String> urlList = picList.stream()
                .map(pic -> {
                    if (group.equals(PatentFileTypeEnum.DESIGN.getCode())) {
                        //外观专利格式需要处理
                        //["立体图:000001.JPG","俯视图:000002.JPG","后视图:000003.JPG","左视图:000004.JPG","主视图:000005.JPG","右视图:000006.JPG","仰视图:000007.JPG"]
                        pic = StrUtil.subAfter(pic, ":", true);
                    }
                    String url = ytService.imgUrl(pid, pic);
                    if (StrUtil.isNotBlank(url)) {
                        return fileService.uploadFileByUrl(url, group, type);
                    } else {
                        return null;
                    }
                })
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        return CollUtil.isEmpty(urlList) ? null : String.join(";", urlList);
    }
}