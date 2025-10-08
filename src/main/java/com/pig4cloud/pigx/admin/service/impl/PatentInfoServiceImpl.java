package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.*;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.dto.patent.cnipr.Legal;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.mapper.PatentInfoMapper;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorUserMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final PatentInventorService patentInventorService;
    private final PatentShelfService patentShelfService;
    private final FileService fileService;
    private final PatentMonitorUserMapper patentMonitorUserMapper;

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


        Map<String, List<PatentInventorEntity>> inventorMap =
                patentInventorService.lambdaQuery()
                        .in(PatentInventorEntity::getPid, pidList)
                        .list()
                        .stream()
                        .collect(Collectors.groupingBy(PatentInventorEntity::getPid));
        String code = SecurityUtils.getUser().getUsername();
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
            // 上下架标识
            boolean shelfFlag = patentShelfService.lambdaQuery()
                    .eq(PatentShelfEntity::getPid, entity.getPid())
                    .exists();
            response.setShelfFlag(shelfFlag ? "1" : "0");
            // 监控标识
            boolean monitorFlag = patentMonitorUserMapper.exists(
                    new LambdaQueryWrapper<PatentMonitorUserEntity>()
                            .eq(PatentMonitorUserEntity::getPid, entity.getPid())
                            .eq(PatentMonitorUserEntity::getCreateUserId, SecurityUtils.getUser().getId())
            );
            response.setMonitorFlag(monitorFlag ? "1" : "0");
            // 认领按钮是否显示
            List<PatentInventorEntity> inventorList = inventorMap.get(entity.getPid());
            boolean showClaimBtn = inventorList != null && inventorList.stream()
                    .anyMatch(inv -> code.equals(inv.getCode()));
            response.setShowClaimBtn(showClaimBtn ? "1" : "0");
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
            String code = "";
            // 数据权限
            DataScope dataScope = DataScope.of();
            if (!dataScopeService.calcScope(dataScope)) {
                deptIds = dataScope.getDeptList();
                code = dataScope.getUsername();

                if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(code)) {
                    log.warn("用户 {} 无数据权限", code);
                    wrapper.apply("1=0"); // 直接返回空结果
                    return wrapper;
                }
            }

            if (ObjectUtil.isNotNull(request.getDeptId())) {
                if (deptIds.contains(request.getDeptId())) {
                    deptIds = CollUtil.newArrayList(request.getDeptId());
                } else {
                    log.warn("用户 {} 无权查看部门 {} 的专利信息", code, request.getDeptId());
                    wrapper.apply("1=0");
                    return wrapper;
                }
            }

            if (CollUtil.isNotEmpty(deptIds) || StrUtil.isNotBlank(code)) {
                String deptIn = CollUtil.isNotEmpty(deptIds)
                        ? deptIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                        : "";
                if (StrUtil.isNotBlank(code) && CollUtil.isNotEmpty(deptIds)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and (code = {0} or dept_id in (" + deptIn + ")))", code);
                } else if (StrUtil.isNotBlank(code)) {
                    wrapper.apply("exists (select 0 from t_patent_inventor " +
                            "where pid = t_patent_info.pid and code = {0})", code);
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
        PatentInfoEntity info = this.lambdaQuery()
                .eq(PatentInfoEntity::getPid, pid)
                .one();

        PatentDetailEntity detail = patentDetailService.lambdaQuery()
                .eq(PatentDetailEntity::getPid, pid)
                .one();

        PatentDetailCacheEntity existing = patentDetailCacheService.lambdaQuery()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .one();

        PatentDetailResponse resp = new PatentDetailResponse();
        if (info != null) {
            BeanUtil.copyProperties(info, resp, CopyOptions.create().ignoreError());
        }
        if (detail != null) {
            BeanUtil.copyProperties(detail, resp, CopyOptions.create().ignoreError());
        }

        /*
         * 仅处理 cache.draws
         * 约定：null 表示从未尝试；"" 表示确认无资源；非空表示已缓存
         */
        if (existing == null) {
            PatentDetailCacheEntity toInsert = new PatentDetailCacheEntity();
            toInsert.setPid(pid);
            toInsert.setTenantId(1L);
            toInsert.setStatus(0);
            toInsert.setDraws(null);

            String stored = "";
            try {
                String absUrl = ytService.absUrl(pid);
                if (StrUtil.isNotBlank(absUrl)) {
                    String uploaded = fileService.uploadFileByUrl(absUrl, PatentFileTypeEnum.ABSTRACT.getCode(), FileGroupTypeEnum.IMAGE);
                    if (StrUtil.isNotBlank(uploaded)) {
                        stored = uploaded;
                    } else {
                        log.warn("uploadFileByUrl 返回空, pid={}, absUrl={}", pid, absUrl);
                    }
                } else {
                    log.warn("absUrl 为空, pid={}", pid);
                }
            } catch (Exception e) {
                log.warn("获取/上传摘要图失败, pid={}, err={}", pid, e.getMessage(), e);
            }
            toInsert.setDraws(stored);

            try {
                patentDetailCacheService.save(toInsert);
            } catch (org.springframework.dao.DuplicateKeyException dup) {
                patentDetailCacheService.lambdaUpdate()
                        .eq(PatentDetailCacheEntity::getPid, pid)
                        .isNull(PatentDetailCacheEntity::getDraws)
                        .set(PatentDetailCacheEntity::getDraws, stored)
                        .update();
            }
        } else if (existing.getDraws() == null) {
            String stored = "";
            try {
                String absUrl = ytService.absUrl(pid);
                if (StrUtil.isNotBlank(absUrl)) {
                    String uploaded = fileService.uploadFileByUrl(absUrl, PatentFileTypeEnum.ABSTRACT.getCode(), FileGroupTypeEnum.IMAGE);
                    if (StrUtil.isNotBlank(uploaded)) {
                        stored = uploaded;
                    } else {
                        log.warn("uploadFileByUrl 返回空, pid={}, absUrl={}", pid, absUrl);
                    }
                } else {
                    log.warn("absUrl 为空, pid={}", pid);
                }
            } catch (Exception e) {
                log.warn("获取/上传摘要图失败, pid={}, err={}", pid, e.getMessage(), e);
            }

            patentDetailCacheService.lambdaUpdate()
                    .eq(PatentDetailCacheEntity::getPid, pid)
                    .isNull(PatentDetailCacheEntity::getDraws)
                    .set(PatentDetailCacheEntity::getDraws, stored)
                    .update();
        }

        PatentDetailCacheEntity latest = patentDetailCacheService.lambdaQuery()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .one();

        resp.setCover(latest != null ? latest.getDraws() : null);
        if (info != null) {
            resp.setId(info.getId());
        }

        this.lambdaUpdate()
                .eq(PatentInfoEntity::getPid, pid)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();

        return resp;
    }


    @Override
    public PatentDetailResponse getDetailImgByPid(String pid) {
        /*
         * 读取基础信息与缓存
         */
        PatentDetailEntity detail = patentDetailService.lambdaQuery()
                .eq(PatentDetailEntity::getPid, pid)
                .one();

        PatentDetailCacheEntity existing = patentDetailCacheService.lambdaQuery()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .one();

        /*
         * 若已有记录且两列都已经尝试过，直接返回
         */
        if (existing != null
                && existing.getDrawsPic() != null
                && existing.getTifDistributePath() != null) {
            return toImgResponse(existing.getDrawsPic(), existing.getTifDistributePath());
        }

        /*
         * 计算需要构建的列，仅对值为 null 的列做一次构建
         */
        String nextDraws = (existing == null || existing.getDrawsPic() == null)
                ? safeBuildBatchPicUrls(
                pid,
                detail != null ? detail.getIncPic() : null,
                PatentFileTypeEnum.SPECIFICATION.getCode(),
                FileGroupTypeEnum.IMAGE,
                ytService)
                : existing.getDrawsPic();

        String nextTif = (existing == null || existing.getTifDistributePath() == null)
                ? safeBuildBatchPicUrls(
                pid,
                detail != null ? detail.getDesignPic() : null,
                PatentFileTypeEnum.DESIGN.getCode(),
                FileGroupTypeEnum.IMAGE,
                ytService)
                : existing.getTifDistributePath();

        /*
         * 将未取到的列落库为空串，以“确认无资源”
         */
        if (existing == null) {
            PatentDetailCacheEntity toInsert = new PatentDetailCacheEntity();
            toInsert.setPid(pid);
            toInsert.setTenantId(1L);
            toInsert.setDrawsPic(StrUtil.emptyToDefault(nextDraws, ""));
            toInsert.setTifDistributePath(StrUtil.emptyToDefault(nextTif, ""));
            try {
                patentDetailCacheService.save(toInsert);
            } catch (org.springframework.dao.DuplicateKeyException dup) {
                /*
                 * 并发下他人先插入，转为条件更新：
                 * 仅当相应列为 NULL 时才写入，避免覆盖他人已写入的值
                 */
                if (nextDraws != null) {
                    patentDetailCacheService.lambdaUpdate()
                            .eq(PatentDetailCacheEntity::getPid, pid)
                            .isNull(PatentDetailCacheEntity::getDrawsPic)
                            .set(PatentDetailCacheEntity::getDrawsPic, StrUtil.emptyToDefault(nextDraws, ""))
                            .update();
                }
                if (nextTif != null) {
                    patentDetailCacheService.lambdaUpdate()
                            .eq(PatentDetailCacheEntity::getPid, pid)
                            .isNull(PatentDetailCacheEntity::getTifDistributePath)
                            .set(PatentDetailCacheEntity::getTifDistributePath, StrUtil.emptyToDefault(nextTif, ""))
                            .update();
                }
            }
        } else {
            /*
             * 已有记录：对每一列分别执行“仅当列为 NULL 时更新”
             */
            if (existing.getDrawsPic() == null) {
                patentDetailCacheService.lambdaUpdate()
                        .eq(PatentDetailCacheEntity::getPid, pid)
                        .isNull(PatentDetailCacheEntity::getDrawsPic)
                        .set(PatentDetailCacheEntity::getDrawsPic, StrUtil.emptyToDefault(nextDraws, ""))
                        .update();
            }
            if (existing.getTifDistributePath() == null) {
                patentDetailCacheService.lambdaUpdate()
                        .eq(PatentDetailCacheEntity::getPid, pid)
                        .isNull(PatentDetailCacheEntity::getTifDistributePath)
                        .set(PatentDetailCacheEntity::getTifDistributePath, StrUtil.emptyToDefault(nextTif, ""))
                        .update();
            }
        }

        /*
         * 读取最新值用于返回；也可直接用 next* 与 existing 组合，但再次读取更稳
         */
        PatentDetailCacheEntity latest = patentDetailCacheService.lambdaQuery()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .one();

        String draws = latest != null ? latest.getDrawsPic() : StrUtil.emptyToDefault(nextDraws, "");
        String tif = latest != null ? latest.getTifDistributePath() : StrUtil.emptyToDefault(nextTif, "");
        return toImgResponse(draws, tif);
    }

    /*
     * 安全封装：入参空直接返回空字符串；异常记录并返回空字符串
     */
    private String safeBuildBatchPicUrls(String pid,
                                         String picSpec,
                                         String fileTypeCode,
                                         FileGroupTypeEnum groupType,
                                         YtService ytService) {
        if (StrUtil.isBlank(picSpec)) {
            return "";
        }
        try {
            return buildBatchPicUrls(pid, picSpec, fileTypeCode, groupType, ytService);
        } catch (Exception e) {
            log.warn("buildBatchPicUrls 失败，pid={}, picSpecLen={}, err={}", pid, picSpec.length(), e.getMessage(), e);
            return "";
        }
    }

    /*
     * 将分号字符串转为响应对象；空或空白返回空列表
     */
    private PatentDetailResponse toImgResponse(String drawsPic, String tifDistributePath) {
        PatentDetailResponse resp = new PatentDetailResponse();
        resp.setDrawsPic(StrUtil.isBlank(drawsPic) ? java.util.Collections.emptyList() : StrUtil.split(drawsPic, ';'));
        resp.setTifDistributePath(StrUtil.isBlank(tifDistributePath) ? java.util.Collections.emptyList() : StrUtil.split(tifDistributePath, ';'));
        return resp;
    }

    @Override
    public String getDetailPdfByPid(String pid) {
        /*
         * 读取现有缓存。
         * 约定：pdf=null 表示从未尝试；pdf="" 表示已确认无资源；pdf=非空URL 表示已缓存。
         * 只要不为 null，就直接返回（包括空串），确保“只拉一次”。
         */
        PatentDetailCacheEntity existing = patentDetailCacheService.lambdaQuery()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .one();
        if (existing != null && existing.getPdf() != null) {
            return StrUtil.emptyToDefault(existing.getPdf(), "");
        }

        /*
         * 准备待保存的实体。
         * 若不存在记录，则新建并初始化基础字段；pdf 先置为 null 表示尚未尝试。
         */
        PatentDetailCacheEntity cache = (existing != null) ? existing : new PatentDetailCacheEntity();
        if (existing == null) {
            cache.setPid(pid);
            cache.setTenantId(1L);
            cache.setStatus(0);
            cache.setPdf(null);
        }

        /*
         * 执行一次获取与上传。
         * 成功则得到上传后的地址；任何失败或无资源情况，统一落库为空串，表示已确认无。
         */
        String stored = "";
        try {
            String srcUrl = ytService.pdfUrl(pid);
            if (StrUtil.isNotBlank(srcUrl)) {
                String uploaded = fileService.uploadFileByUrl(srcUrl, PatentFileTypeEnum.PDF.getCode(), FileGroupTypeEnum.FILE);
                if (StrUtil.isNotBlank(uploaded)) {
                    stored = uploaded;
                } else {
                    log.warn("uploadFileByUrl 返回空，pid={}, srcUrl={}", pid, srcUrl);
                }
            } else {
                log.warn("pdfUrl 为空，pid={}", pid);
            }
        } catch (Exception e) {
            log.warn("获取/上传 PDF 失败，pid={}，err={}", pid, e.getMessage(), e);
        }
        cache.setPdf(stored);

        /*
         * 并发安全的 upsert。
         * 先按 pid 更新；若未命中，再尝试插入；
         * 插入若遇唯一键冲突，说明并发下他人已插入，则回退为按 pid 再次更新。
         * 建议为 pid 建立唯一索引以保障幂等。
         */
        boolean updated = patentDetailCacheService.lambdaUpdate()
                .eq(PatentDetailCacheEntity::getPid, pid)
                .set(PatentDetailCacheEntity::getPdf, cache.getPdf())
                .update();

        if (!updated) {
            try {
                patentDetailCacheService.save(cache);
            } catch (org.springframework.dao.DuplicateKeyException dup) {
                patentDetailCacheService.lambdaUpdate()
                        .eq(PatentDetailCacheEntity::getPid, pid)
                        .set(PatentDetailCacheEntity::getPdf, cache.getPdf())
                        .update();
            }
        }

        /*
         * 返回结果。
         * 空串代表已确认无资源，调用方可据此展示“无”或占位。
         */
        return StrUtil.emptyToDefault(cache.getPdf(), "");
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

    /**
     * 专利-申请公开
     * 依据：t_patent_info.pubDate 在 [start, end] 且 patType 属于目标类型
     */
    @Override
    public List<PerfEventDTO> fetchPatentApplyPub(IpTypeEnum type,
                                                  PerfRuleEntity rule,
                                                  LocalDate start,
                                                  LocalDate end) {
        List<String> patTypeNames = mapPatTypeNames(type);
        if (patTypeNames.isEmpty()) return Collections.emptyList();

        QueryWrapper<PatentInfoEntity> qw = new QueryWrapper<>();
        qw.in("pat_type", patTypeNames)
                .eq(patTypeNames.contains(PatentTypeEnum.INVENTION.getCode()), "db_name", PatentDbNameEnum.FMZL.getCode())
                .apply(start != null,
                        "COALESCE(STR_TO_DATE(pub_date,'%Y.%m.%d')) >= {0}",
                        start)
                .apply(end != null,
                        "COALESCE(STR_TO_DATE(pub_date,'%Y.%m.%d')) <= {0}",
                        end);

        List<PatentInfoEntity> list = this.list(qw);
        if (list.isEmpty()) return Collections.emptyList();

        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (PatentInfoEntity r : list) {
            LocalDate pub = parseLocalDate(r.getPubDate());
            if (pub == null) continue;

            List<PerfParticipantDTO> ps = loadPatentParticipantsByPid(r.getPid());
            out.add(PerfEventDTO.builder()
                    .pid(r.getPid())
                    .eventTime(pub.atStartOfDay())
                    .ipTypeCode(type.getCode())
                    .ipTypeName(type.getName())
                    .ruleEventCode(RuleEventEnum.APPLY_PUB.getCode())
                    .ruleEventName(RuleEventEnum.APPLY_PUB.getName())
                    .baseScore(rule.getScore())
                    .participants(ps)
                    .build());
        }
        return out;
    }

    /**
     * 专利-授权公告
     * 查询：按类型与授权公告日过滤；表字段示例：pid, grant_publish_date
     */
    @Override
    public List<PerfEventDTO> fetchPatentGrantPub(IpTypeEnum type,
                                                  PerfRuleEntity rule,
                                                  LocalDate start,
                                                  LocalDate end) {
        List<String> patTypeNames = mapPatTypeNames(type);
        if (patTypeNames.isEmpty()) return Collections.emptyList();

        QueryWrapper<PatentInfoEntity> qw = new QueryWrapper<>();
        qw.in("pat_type", patTypeNames)
                .eq(patTypeNames.contains(PatentTypeEnum.INVENTION.getCode()), "db_name", PatentDbNameEnum.FMSQ.getCode())
                .apply(start != null,
                        "COALESCE(STR_TO_DATE(pub_date,'%Y.%m.%d')) >= {0}",
                        start)
                .apply(end != null,
                        "COALESCE(STR_TO_DATE(pub_date,'%Y.%m.%d')) <= {0}",
                        end);

        List<PatentInfoEntity> list = this.list(qw);
        if (list.isEmpty()) return Collections.emptyList();

        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (PatentInfoEntity r : list) {
            LocalDate pub = parseLocalDate(r.getPubDate());
            if (pub == null) continue;

            List<PerfParticipantDTO> ps = loadPatentParticipantsByPid(r.getPid());
            out.add(PerfEventDTO.builder()
                    .pid(r.getPid())
                    .eventTime(pub.atStartOfDay())
                    .ipTypeCode(type.getCode())
                    .ipTypeName(type.getName())
                    .ruleEventCode(RuleEventEnum.GRANT_PUB.getCode())
                    .ruleEventName(RuleEventEnum.GRANT_PUB.getName())
                    .baseScore(rule.getScore())
                    .participants(ps)
                    .build());
        }
        return out;
    }

    private List<PerfParticipantDTO> loadPatentParticipantsByPid(String pid) {
        if (pid == null || pid.isEmpty()) {
            return Collections.emptyList();
        }
        List<PatentInventorEntity> list = patentInventorService.lambdaQuery()
                .eq(PatentInventorEntity::getPid, pid)
                .list();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(inv -> PerfParticipantDTO.builder()
                        .userId(null) // 如能从工号反查用户ID，可在此补齐
                        .userCode(inv.getCode())
                        .userName(inv.getName())
                        .deptId(inv.getDeptId())
                        .deptName(inv.getDeptName())
                        .priority(parseInt(inv.getPriority()))
                        .isLeader(inv.getIsLeader())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 安全解析顺位
     */
    private Integer parseInt(String s) {
        try {
            return s == null ? null : Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试多种常见格式解析 LocalDate
     * 支持：yyyy-MM-dd, yyyyMMdd, yyyy/MM/dd, yyyy.MM.dd
     */
    private LocalDate parseLocalDate(String s) {
        if (s == null) return null;
        String v = s.trim();
        if (v.isEmpty()) return null;
        String[] patterns = {"yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy.MM.dd"};
        for (String p : patterns) {
            try {
                return java.time.LocalDate.parse(v, java.time.format.DateTimeFormatter.ofPattern(p));
            } catch (Exception ignore) {
            }
        }
        // 兜底：只取前10位尝试 yyyy-MM-dd
        if (v.length() >= 10) {
            try {
                return java.time.LocalDate.parse(v.substring(0, 10));
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * 将 IpTypeEnum 映射到专利库里的 patType 文本集合
     */
    private List<String> mapPatTypeNames(IpTypeEnum type) {
        if (type == null) {
            return Collections.emptyList();
        }
        switch (type) {
            case INVENTION:
                return Arrays.asList(PatentTypeEnum.INVENTION.getCode());
            case UTILITY_MODEL:
                return Arrays.asList(PatentTypeEnum.UTILITY_MODEL.getCode());
            case DESIGN:
                return Arrays.asList(PatentTypeEnum.DESIGN.getCode());
            default:
                return Collections.emptyList();
        }
    }
}