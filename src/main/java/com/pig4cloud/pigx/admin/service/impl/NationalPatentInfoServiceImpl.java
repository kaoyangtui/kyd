package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pig4cloud.pigx.admin.constants.*;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowPageReq;
import com.pig4cloud.pigx.admin.dto.patent.PatentDetailResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchListReq;
import com.pig4cloud.pigx.admin.dto.patent.cnipr.Legal;
import com.pig4cloud.pigx.admin.entity.NationalPatentDetailEntity;
import com.pig4cloud.pigx.admin.entity.NationalPatentFollowEntity;
import com.pig4cloud.pigx.admin.entity.NationalPatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.mapper.NationalPatentInfoMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.NationalPatentDetailService;
import com.pig4cloud.pigx.admin.service.NationalPatentFollowService;
import com.pig4cloud.pigx.admin.service.NationalPatentInfoService;
import com.pig4cloud.pigx.admin.service.YtService;
import com.pig4cloud.pigx.admin.utils.CniprExpUtils;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NationalPatentInfoServiceImpl extends ServiceImpl<NationalPatentInfoMapper, NationalPatentInfoEntity> implements NationalPatentInfoService {

    private final YtService ytService;
    private final NationalPatentDetailService nationalPatentDetailService;
    private final NationalPatentFollowService nationalPatentFollowService;
    private final FileService fileService;
    private static final ExecutorService IMAGE_CACHE_EXECUTOR = new ThreadPoolExecutor(
            2,
            4,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new ThreadFactory() {
                private int idx = 1;

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "patent-img-cache-" + idx++);
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Override
    public IPage<PatentInfoResponse> searchList(PatentSearchListReq req) {
        // === 1) 条件表达式构建 ===
        List<String> exps = Lists.newArrayList("申请国代码=(CN)");

        // 1) 专利名称
        addEq(exps, CnirpExpConstants.TITLE, req.getPatentTitle());

        // 2) 申请号
        addEq(exps, CnirpExpConstants.APP_NUMBER, req.getAppNumber());

        // 3) 摘要
        addEq(exps, CnirpExpConstants.ABS, req.getAbs());

        // 4) 专利类型
        addIn(exps, CnirpExpConstants.PAT_TYPE, req.getPatentTypeArray());

        // 5) 法律状态
        addIn(exps, CnirpExpConstants.LEGAL_STATUS, req.getLegalStatusArray());

        // 6) 技术领域（IPC分类）
        addIn(exps, CnirpExpConstants.IPC, req.getIpcArray());
        addIn(exps, CnirpExpConstants.IPC_SECTION, req.getIpcSectionArray());
        addIn(exps, CnirpExpConstants.IPC_CLASS, req.getIpcClassArray());
        addIn(exps, CnirpExpConstants.IPC_SUB_CLASS, req.getIpcSubClassArray());

        // 7) 申请日（日期段）
        addBetween(exps, CnirpExpConstants.APP_DATE, req.getApplicationDateRange());

        // 8) 公开号日（日期段）
        addBetween(exps, CnirpExpConstants.PUB_DATE, req.getPublicationDateRange());

        // 9) 申请人/权利人
        addEq(exps, CnirpExpConstants.APPLICANT_NAME, req.getCurrentApplicantOrOwner());

        // 10) 发明人
        addEq(exps, CnirpExpConstants.INVENTOR_NAME, req.getInventor());

        // 11) 高价值发明专利：单个布尔标识 + 多标签集合（都转成 code=1）
        if (req.getHighValueFlag() != null) {
            exps.add(CniprExpUtils.getExpEq(CnirpExpConstants.HIGH_VALUE_FLAG, String.valueOf(req.getHighValueFlag())));
        }
        if (CollUtil.isNotEmpty(req.getHighValueArray())) {
            Map<String, String> map = new HashMap<>(req.getHighValueArray().size());
            for (String item : req.getHighValueArray()) {
                map.put(item, "1");
            }
            exps.add(CniprExpUtils.getExpMap(map));
        }

        // 合并最终表达式
        String exp = CniprExpUtils.getExpAnd(exps);
        log.info("CNIPR exp: {}", exp);

        // === 2) 排序 ===
        String order;
        switch (req.getSortType() == null ? -1 : req.getSortType()) {
            case 1:
                order = "-appDate";
                break; // 申请日倒序
            case 2:
                order = "+appDate";
                break; // 申请日正序（示例）
            default:
                order = "-pubDate";
                break; // 默认
        }

        // === 3) 调三方检索（YtService 只负责出数+计数，不落库） ===
        String dbs = "FMZL,FMSQ,SYXX,WGZL,USPATENT,GBPATENT,FRPATENT,DEPATENT,CHPATENT,JPPATENT,RUPATENT,KRPATENT,EPPATENT,WOPATENT";
        int option = 2; // 按字检索
        String displayCols = CnirpDisplayColsConstants.ALL_FIELDS;
        boolean highLight = false;
        boolean isDbAgg = false;

        int from = req.getFrom();
        int size = req.getSize();

        Page<PatentLogEntity> thirdPage = ytService.page(exp, dbs, option, order, from, size, displayCols, highLight, isDbAgg);

        // === 4) 批量落库（放在业务层） ===
        List<String> messages = thirdPage.getRecords().stream()
                .map(PatentLogEntity::getResponseBody)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!messages.isEmpty()) {
            this.upsertBatchFromMessages(messages, 500);
            nationalPatentDetailService.upsertBatchFromMessages(messages, 500);
            cacheImagesAsync(messages);
        }

        // === 5) 批量判定“是否已关注” ===
        Long userId = SecurityUtils.getUser().getId();
        List<String> pids = thirdPage.getRecords().stream()
                .map(pl -> JSONUtil.parseObj(pl.getResponseBody()).getStr("pid"))
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> localDrawsMap = pids.isEmpty()
                ? Collections.emptyMap()
                : this.lambdaQuery()
                .select(NationalPatentInfoEntity::getPid, NationalPatentInfoEntity::getDraws)
                .in(NationalPatentInfoEntity::getPid, pids)
                .list()
                .stream()
                .filter(it -> StrUtil.isNotBlank(it.getPid()))
                .collect(Collectors.toMap(NationalPatentInfoEntity::getPid, NationalPatentInfoEntity::getDraws, (a, b) -> a));

        Set<String> followedPidSet;
        if (!pids.isEmpty()) {
            // 这里用你的关注表 Service 批量查
            // NationalPatentFollowService 里可以提供：listFollowedPids(userId, pids) -> Set<String>
            followedPidSet = nationalPatentFollowService.listFollowedPids(userId, pids);
        } else {
            followedPidSet = Collections.emptySet();
        }

        // === 6) 转 DTO，并赋值 followFlag ===
        return thirdPage.convert(entity -> {
            PatentInfoResponse dto = JSONUtil.toBean(entity.getResponseBody(), PatentInfoResponse.class);
            PatentTypeEnum type = PatentTypeEnum.getByCode(dto.getPatType());
            dto.setPatTypeName(type != null ? type.getDescription() : "未知");
            dto.setFollowFlag(followedPidSet.contains(dto.getPid()) ? "1" : "0");
            String localDraws = localDrawsMap.get(dto.getPid());
            if (StrUtil.isNotBlank(localDraws)) {
                dto.setDraws(localDraws);
            }
            return dto;
        });
    }

    /* ======================= 私有工具方法 ======================= */

    /**
     * 等值：非空才加
     */
    private void addEq(List<String> exps, String field, String val) {
        if (StrUtil.isNotBlank(val)) {
            exps.add(CniprExpUtils.getExpEq(field, val.trim()));
        }
    }

    /**
     * IN：非空集合才加
     */
    private void addIn(List<String> exps, String field, List<String> vals) {
        if (CollUtil.isNotEmpty(vals)) {
            exps.add(CniprExpUtils.getExpIn(field, vals));
        }
    }

    /**
     * between：长度为2且都不为空才加（字符串日期直传）
     */
    private void addBetween(List<String> exps, String field, String[] range) {
        if (range != null && range.length == 2 && StrUtil.isNotBlank(range[0]) && StrUtil.isNotBlank(range[1])) {
            exps.add(CniprExpUtils.getExpBetween(field, range[0].trim(), range[1].trim()));
        }
    }


    @Override
    public void create(NationalPatentInfoEntity patentInfo, String message) {
        normalizeAndDerive(patentInfo, message);
        NationalPatentInfoEntity oldPatentInfo = this.lambdaQuery().eq(NationalPatentInfoEntity::getPid, patentInfo.getPid()).one();
        if (oldPatentInfo != null) {
            BeanUtil.copyProperties(patentInfo, oldPatentInfo, CopyOptions.create().setIgnoreNullValue(true));
            this.updateById(oldPatentInfo);
            log.info("更新成功: {}", oldPatentInfo);
        } else {
            setDefaultsOnInsert(patentInfo);
            this.save(patentInfo);
            log.info("保存成功: {}", patentInfo);
            this.lambdaUpdate()
                    .eq(NationalPatentInfoEntity::getAppNumber, patentInfo.getAppNumber())
                    .ne(NationalPatentInfoEntity::getPid, patentInfo.getPid())
                    .set(NationalPatentInfoEntity::getMergeFlag, "0")
                    .update();
        }
    }

    @Override
    public void upsertBatchFromMessages(List<String> messages, int batchSize) {
        if (CollUtil.isEmpty(messages)) return;

        List<NationalPatentInfoEntity> candidates = new ArrayList<>(messages.size());
        for (String msg : messages) {
            NationalPatentInfoEntity e = JSONUtil.toBean(msg, NationalPatentInfoEntity.class);
            e.setTenantId(1L);
            normalizeAndDerive(e, msg);
            candidates.add(e);
        }

        Map<String, NationalPatentInfoEntity> byPid = candidates.stream()
                .filter(it -> StrUtil.isNotBlank(it.getPid()))
                .collect(Collectors.toMap(NationalPatentInfoEntity::getPid, it -> it, (a, b) -> a, LinkedHashMap::new));

        if (byPid.isEmpty()) return;

        List<String> pids = new ArrayList<>(byPid.keySet());
        List<NationalPatentInfoEntity> existList = this.list(new LambdaQueryWrapper<NationalPatentInfoEntity>()
                .in(NationalPatentInfoEntity::getPid, pids));

        Set<String> existPidSet = existList.stream().map(NationalPatentInfoEntity::getPid).collect(Collectors.toSet());

        List<NationalPatentInfoEntity> toInsert = new ArrayList<>();
        List<NationalPatentInfoEntity> toUpdate = new ArrayList<>();

        for (Map.Entry<String, NationalPatentInfoEntity> en : byPid.entrySet()) {
            String pid = en.getKey();
            NationalPatentInfoEntity cur = en.getValue();
            if (existPidSet.contains(pid)) {
                NationalPatentInfoEntity old = existList.stream().filter(x -> pid.equals(x.getPid())).findFirst().orElse(null);
                if (old != null) {
                    BeanUtil.copyProperties(cur, old, CopyOptions.create().setIgnoreNullValue(true));
                    toUpdate.add(old);
                }
            } else {
                setDefaultsOnInsert(cur);
                toInsert.add(cur);
            }
        }

        if (CollUtil.isNotEmpty(toInsert)) {
            this.saveBatch(toInsert, Math.max(200, batchSize));
        }
        if (CollUtil.isNotEmpty(toUpdate)) {
            this.updateBatchById(toUpdate, Math.max(200, batchSize));
        }

        if (CollUtil.isNotEmpty(toInsert)) {
            Map<String, List<NationalPatentInfoEntity>> byApp = toInsert.stream()
                    .filter(it -> StrUtil.isNotBlank(it.getAppNumber()))
                    .collect(Collectors.groupingBy(NationalPatentInfoEntity::getAppNumber));

            for (Map.Entry<String, List<NationalPatentInfoEntity>> entry : byApp.entrySet()) {
                String appNo = entry.getKey();
                List<String> newPids = entry.getValue().stream().map(NationalPatentInfoEntity::getPid).collect(Collectors.toList());
                if (StrUtil.isBlank(appNo) || CollUtil.isEmpty(newPids)) continue;

                this.lambdaUpdate()
                        .eq(NationalPatentInfoEntity::getAppNumber, appNo)
                        .notIn(NationalPatentInfoEntity::getPid, newPids)
                        .set(NationalPatentInfoEntity::getMergeFlag, "0")
                        .update();
            }
        }
    }

    @Override
    public PatentDetailResponse getDetailByPid(String pid) {
        NationalPatentInfoEntity info = this.lambdaQuery().eq(NationalPatentInfoEntity::getPid, pid).one();
        NationalPatentDetailEntity detail = nationalPatentDetailService.lambdaQuery().eq(NationalPatentDetailEntity::getPid, pid).one();

        cacheDetailImagesAsync(info, detail);

        PatentDetailResponse resp = new PatentDetailResponse();
        if (info != null) {
            BeanUtil.copyProperties(info, resp, CopyOptions.create().ignoreError());
        }
        if (detail != null) {
            BeanUtil.copyProperties(detail, resp, CopyOptions.create().ignoreError());
        }
        resp.setCover(detail.getDraws());
        resp.setId(info.getId());
        return resp;
    }


    @Override
    public IPage<PatentInfoResponse> pageMyFollow(Long userId, NationalPatentFollowPageReq req) {
        // 1) 分页查关注表（支持 keyword 在 tags 上 LIKE）
        Page<NationalPatentFollowEntity> p = new Page<>(req.getPageNo(), req.getPageSize());
        String kw = StrUtil.trimToNull(req.getKeyword());

        LambdaQueryWrapper<NationalPatentFollowEntity> qw = Wrappers.lambdaQuery(NationalPatentFollowEntity.class)
                .eq(NationalPatentFollowEntity::getUserId, userId)
                .like(kw != null, NationalPatentFollowEntity::getTags, kw)
                .orderByDesc(NationalPatentFollowEntity::getCreateTime);

        nationalPatentFollowService.page(p, qw);

        List<NationalPatentFollowEntity> rows = p.getRecords();
        if (CollUtil.isEmpty(rows)) {
            return new Page<>(req.getPageNo(), req.getPageSize(), p.getTotal());
        }

        // 2) 批量取主表（保持 rows 顺序）
        List<String> pids = rows.stream()
                .map(NationalPatentFollowEntity::getPid)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(pids)) {
            return new Page<>(req.getPageNo(), req.getPageSize(), p.getTotal());
        }

        List<NationalPatentInfoEntity> infos = this.lambdaQuery()
                .in(NationalPatentInfoEntity::getPid, pids)
                .list();
        Map<String, NationalPatentInfoEntity> infoMap = infos.stream()
                .collect(Collectors.toMap(NationalPatentInfoEntity::getPid, x -> x, (a, b) -> a));

        // 3) 组装返回 DTO，保持关注顺序；空缺跳过
        List<PatentInfoResponse> result = new ArrayList<>(rows.size());
        for (NationalPatentFollowEntity f : rows) {
            NationalPatentInfoEntity e = infoMap.get(f.getPid());
            if (e == null) {
                continue; // 主表不存在就跳过，避免 NPE
            }
            PatentInfoResponse dto = new PatentInfoResponse();
            BeanUtil.copyProperties(e, dto, CopyOptions.create().ignoreError());
            PatentTypeEnum type = PatentTypeEnum.getByCode(dto.getPatType());
            dto.setPatTypeName(type != null ? type.getDescription() : "未知");
            dto.setFollowFlag("1");
            result.add(dto);
        }

        Page<PatentInfoResponse> out = new Page<>(req.getPageNo(), req.getPageSize(), p.getTotal());
        out.setRecords(result);
        return out;
    }

    private void normalizeAndDerive(NationalPatentInfoEntity patentInfo, String message) {
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

        if (StrUtil.isNotBlank(patentInfo.getPubDate())
                && StrUtil.isBlank(patentInfo.getExaminationDate())
                && StrUtil.isBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.PUBLICATION.getDesc());
        } else if (StrUtil.isNotBlank(patentInfo.getExaminationDate())
                && StrUtil.isBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.EXAMINATION.getDesc());
        } else if (StrUtil.isNotBlank(patentInfo.getUnAuthorizedDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.UNAUTHORIZED.getDesc());
        } else if (StrUtil.isNotBlank(patentInfo.getGrantDate())
                && StrUtil.isBlank(patentInfo.getUnAuthorizedDate())
                && StrUtil.isBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.EFFECTIVE.getDesc());
        } else if (StrUtil.isNotBlank(patentInfo.getUnEffectiveDate())) {
            patentInfo.setLegalStatus(DiPatentLegalStatusEnum.UNEFFECTIVE.getDesc());
        }

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
    }

    private void setDefaultsOnInsert(NationalPatentInfoEntity patentInfo) {
        patentInfo.setMergeFlag("1");
        patentInfo.setClaimFlag("0");
        patentInfo.setShelfFlag("0");
        if (patentInfo.getViewCount() == null) {
            patentInfo.setViewCount(0L);
        }
    }

    private String calcTransferFlag(String applicantName, String patentee) {
        List<String> applicants = StrUtil.splitTrim(applicantName, ";");
        List<String> patentees = StrUtil.splitTrim(patentee, ";");
        return CollUtil.isNotEmpty(applicants) && CollUtil.isNotEmpty(patentees)
                && !CollUtil.containsAny(patentees, applicants)
                ? "1" : "0";
    }

    private String cacheImageUrl(String url, String dir) {
        if (StrUtil.isBlank(url) || isLocalUrl(url)) {
            return url;
        }
        try {
            String uploaded = fileService.uploadFileByUrl(url, dir, FileGroupTypeEnum.IMAGE);
            return StrUtil.isNotBlank(uploaded) ? uploaded : url;
        } catch (Exception e) {
            log.warn("缓存图片失败, url={}, err={}", url, e.getMessage());
            return url;
        }
    }

    private String cacheUrlList(String raw, String dir) {
        if (StrUtil.isBlank(raw)) {
            return raw;
        }
        boolean rawIsArray = raw.trim().startsWith("[");
        List<String> urls = parseUrlList(raw);
        if (CollUtil.isEmpty(urls)) {
            return raw;
        }
        List<String> cached = new ArrayList<>(urls.size());
        for (String url : urls) {
            cached.add(cacheImageUrl(url, dir));
        }
        if (!rawIsArray && cached.size() == 1) {
            return cached.get(0);
        }
        return JSONUtil.toJsonStr(cached);
    }

    private List<String> parseUrlList(String raw) {
        if (StrUtil.isBlank(raw)) {
            return Collections.emptyList();
        }
        try {
            if (raw.trim().startsWith("[")) {
                return JSONUtil.parseArray(raw).toList(String.class);
            }
        } catch (Exception ignored) {
        }
        return StrUtil.isNotBlank(raw) ? Collections.singletonList(raw) : Collections.emptyList();
    }

    private boolean isLocalUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return false;
        }
        String trimmed = url.trim();
        if (trimmed.startsWith("[")) {
            return false;
        }
        if (!url.startsWith("http")) {
            return true;
        }
        return url.contains("/admin/sys-file/oss/file")
                || url.contains("/sys-file/oss/file");
    }

    private void cacheImagesAsync(List<String> messages) {
        if (CollUtil.isEmpty(messages)) {
            return;
        }
        IMAGE_CACHE_EXECUTOR.execute(() -> {
            try {
                List<JSONObject> objs = new ArrayList<>(messages.size());
                List<String> pids = new ArrayList<>();
                for (String msg : messages) {
                    if (StrUtil.isBlank(msg)) {
                        continue;
                    }
                    JSONObject obj = JSONUtil.parseObj(msg);
                    objs.add(obj);
                    String pid = obj.getStr("pid");
                    if (StrUtil.isNotBlank(pid)) {
                        pids.add(pid);
                    }
                }
                if (pids.isEmpty()) {
                    return;
                }
                Map<String, NationalPatentInfoEntity> infoMap = this.lambdaQuery()
                        .in(NationalPatentInfoEntity::getPid, pids)
                        .list()
                        .stream()
                        .collect(Collectors.toMap(NationalPatentInfoEntity::getPid, it -> it, (a, b) -> a));
                Map<String, NationalPatentDetailEntity> detailMap = nationalPatentDetailService.lambdaQuery()
                        .in(NationalPatentDetailEntity::getPid, pids)
                        .list()
                        .stream()
                        .collect(Collectors.toMap(NationalPatentDetailEntity::getPid, it -> it, (a, b) -> a));

                for (JSONObject obj : objs) {
                    String pid = obj.getStr("pid");
                    if (StrUtil.isBlank(pid)) {
                        continue;
                    }
                    String draws = obj.getStr("draws");
                    if (StrUtil.isNotBlank(draws)) {
                        NationalPatentInfoEntity info = infoMap.get(pid);
                        if (info == null || StrUtil.isBlank(info.getDraws())) {
                            this.lambdaUpdate()
                                    .eq(NationalPatentInfoEntity::getPid, pid)
                                    .set(NationalPatentInfoEntity::getDraws, draws)
                                    .update();
                        }
                        if (info == null || !isLocalUrl(info.getDraws())) {
                            String cached = cacheImageUrl(draws, PatentFileTypeEnum.ABSTRACT.getCode());
                            if (StrUtil.isNotBlank(cached) && !cached.equals(draws) && isLocalUrl(cached)) {
                                this.lambdaUpdate()
                                        .eq(NationalPatentInfoEntity::getPid, pid)
                                        .set(NationalPatentInfoEntity::getDraws, cached)
                                        .update();
                            }
                        }
                    }

                    NationalPatentDetailEntity detail = detailMap.get(pid);
                    if (detail == null) {
                        continue;
                    }
                    boolean changed = false;
                    String rawDetailDraws = obj.getStr("draws");
                    if (StrUtil.isNotBlank(rawDetailDraws)) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, pid)
                                .isNull(NationalPatentDetailEntity::getDraws)
                                .set(NationalPatentDetailEntity::getDraws, rawDetailDraws)
                                .update();
                    }
                    String rawDrawsPic = obj.getStr("drawsPic");
                    if (StrUtil.isNotBlank(rawDrawsPic)) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, pid)
                                .isNull(NationalPatentDetailEntity::getDrawsPic)
                                .set(NationalPatentDetailEntity::getDrawsPic, rawDrawsPic)
                                .update();
                    }
                    String rawTif = obj.getStr("tifDistributePath");
                    if (StrUtil.isNotBlank(rawTif)) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, pid)
                                .isNull(NationalPatentDetailEntity::getTifDistributePath)
                                .set(NationalPatentDetailEntity::getTifDistributePath, rawTif)
                                .update();
                    }
                    if (StrUtil.isNotBlank(detail.getDraws()) && !isLocalUrl(detail.getDraws())) {
                        String cachedDraws = cacheImageUrl(detail.getDraws(), PatentFileTypeEnum.ABSTRACT.getCode());
                        if (StrUtil.isNotBlank(cachedDraws) && !cachedDraws.equals(detail.getDraws()) && isLocalUrl(cachedDraws)) {
                            detail.setDraws(cachedDraws);
                            changed = true;
                        }
                    }
                    if (StrUtil.isNotBlank(detail.getDrawsPic()) && !isLocalUrl(detail.getDrawsPic())) {
                        String cachedDrawsPic = cacheUrlList(detail.getDrawsPic(), PatentFileTypeEnum.SPECIFICATION.getCode());
                        if (StrUtil.isNotBlank(cachedDrawsPic) && !cachedDrawsPic.equals(detail.getDrawsPic())) {
                            detail.setDrawsPic(cachedDrawsPic);
                            changed = true;
                        }
                    }
                    if (StrUtil.isNotBlank(detail.getTifDistributePath()) && !isLocalUrl(detail.getTifDistributePath())) {
                        String cachedDesign = cacheUrlList(detail.getTifDistributePath(), PatentFileTypeEnum.DESIGN.getCode());
                        if (StrUtil.isNotBlank(cachedDesign) && !cachedDesign.equals(detail.getTifDistributePath())) {
                            detail.setTifDistributePath(cachedDesign);
                            changed = true;
                        }
                    }
                    if (changed) {
                        nationalPatentDetailService.updateById(detail);
                    }
                }
            } catch (Exception e) {
                log.warn("异步缓存专利图片失败: {}", e.getMessage());
            }
        });
    }

    private void cacheDetailImagesAsync(NationalPatentInfoEntity info, NationalPatentDetailEntity detail) {
        if (info == null && detail == null) {
            return;
        }
        IMAGE_CACHE_EXECUTOR.execute(() -> {
            try {
                if (info != null && StrUtil.isNotBlank(info.getDraws()) && !isLocalUrl(info.getDraws())) {
                    this.lambdaUpdate()
                            .eq(NationalPatentInfoEntity::getPid, info.getPid())
                            .isNull(NationalPatentInfoEntity::getDraws)
                            .set(NationalPatentInfoEntity::getDraws, info.getDraws())
                            .update();
                    String cached = cacheImageUrl(info.getDraws(), PatentFileTypeEnum.ABSTRACT.getCode());
                    if (StrUtil.isNotBlank(cached) && !cached.equals(info.getDraws()) && isLocalUrl(cached)) {
                        this.lambdaUpdate()
                                .eq(NationalPatentInfoEntity::getPid, info.getPid())
                                .set(NationalPatentInfoEntity::getDraws, cached)
                                .update();
                    }
                }
                if (detail != null) {
                    boolean changed = false;
                    if (StrUtil.isNotBlank(detail.getDraws())) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, detail.getPid())
                                .isNull(NationalPatentDetailEntity::getDraws)
                                .set(NationalPatentDetailEntity::getDraws, detail.getDraws())
                                .update();
                    }
                    if (StrUtil.isNotBlank(detail.getDrawsPic())) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, detail.getPid())
                                .isNull(NationalPatentDetailEntity::getDrawsPic)
                                .set(NationalPatentDetailEntity::getDrawsPic, detail.getDrawsPic())
                                .update();
                    }
                    if (StrUtil.isNotBlank(detail.getTifDistributePath())) {
                        nationalPatentDetailService.lambdaUpdate()
                                .eq(NationalPatentDetailEntity::getPid, detail.getPid())
                                .isNull(NationalPatentDetailEntity::getTifDistributePath)
                                .set(NationalPatentDetailEntity::getTifDistributePath, detail.getTifDistributePath())
                                .update();
                    }
                    if (StrUtil.isNotBlank(detail.getDraws()) && !isLocalUrl(detail.getDraws())) {
                        String cachedDraws = cacheImageUrl(detail.getDraws(), PatentFileTypeEnum.ABSTRACT.getCode());
                        if (StrUtil.isNotBlank(cachedDraws) && !cachedDraws.equals(detail.getDraws()) && isLocalUrl(cachedDraws)) {
                            detail.setDraws(cachedDraws);
                            changed = true;
                        }
                    }
                    if (StrUtil.isNotBlank(detail.getDrawsPic()) && !isLocalUrl(detail.getDrawsPic())) {
                        String cachedDrawsPic = cacheUrlList(detail.getDrawsPic(), PatentFileTypeEnum.SPECIFICATION.getCode());
                        if (StrUtil.isNotBlank(cachedDrawsPic) && !cachedDrawsPic.equals(detail.getDrawsPic())) {
                            detail.setDrawsPic(cachedDrawsPic);
                            changed = true;
                        }
                    }
                    if (StrUtil.isNotBlank(detail.getTifDistributePath()) && !isLocalUrl(detail.getTifDistributePath())) {
                        String cachedDesign = cacheUrlList(detail.getTifDistributePath(), PatentFileTypeEnum.DESIGN.getCode());
                        if (StrUtil.isNotBlank(cachedDesign) && !cachedDesign.equals(detail.getTifDistributePath())) {
                            detail.setTifDistributePath(cachedDesign);
                            changed = true;
                        }
                    }
                    if (changed) {
                        nationalPatentDetailService.updateById(detail);
                    }
                }
            } catch (Exception e) {
                log.warn("异步缓存详情图片失败: {}", e.getMessage());
            }
        });
    }
}
