package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.PatentStatusEnum;
import com.pig4cloud.pigx.admin.constants.PatentTypeEnum;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.match.DemandMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.PatentMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.ResultMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.SupplyDemandMatchRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MatchServiceImpl implements MatchService {

    private final DemandServiceImpl demandService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;
    private final DimEcService dimEcService;
    private static final int MATCH_BATCH_SIZE = 100;

    /**
     * 计算匹配并批量更新：需求/专利/成果 的 maxMatchScore
     */
    @Override
    public Boolean demandMatch() {
        // 1) NEC code -> name 映射
        Map<String, String> necNameMap = dimEcService.lambdaQuery()
                .list()
                .stream()
                .filter(e -> StrUtil.isNotBlank(e.getCode()))
                .collect(Collectors.toMap(
                        DimEcEntity::getCode,
                        DimEcEntity::getName,
                        (a, b) -> a
                ));

        // 2) 拉取数据
        List<DemandEntity> demandList = demandService.lambdaQuery()
                .eq(DemandEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();

        List<PatentInfoEntity> patentList = patentInfoService.lambdaQuery()
                .eq(PatentInfoEntity::getStatusCode, PatentStatusEnum.VALID.getCode())
                .eq(PatentInfoEntity::getMergeFlag, "1")
                .in(PatentInfoEntity::getPatType,
                        PatentTypeEnum.INVENTION.getCode(),
                        PatentTypeEnum.UTILITY_MODEL.getCode())
                .list();

        List<ResultEntity> resultList = resultService.lambdaQuery()
                .eq(ResultEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();

        log.info("[MatchJob] start demandMatch: demand={}, patent={}, result={}",
                demandList.size(), patentList.size(), resultList.size());

        // 2.1 当前库中的最高分快照
        Map<Long, Long> demandCurrentMax = demandList.stream()
                .collect(Collectors.toMap(DemandEntity::getId, d -> defaultZero(d.getMaxMatchScore())));
        Map<Long, Long> patentCurrentMax = patentList.stream()
                .collect(Collectors.toMap(PatentInfoEntity::getId, p -> defaultZero(p.getMaxMatchScore())));
        Map<Long, Long> resultCurrentMax = resultList.stream()
                .collect(Collectors.toMap(ResultEntity::getId, r -> defaultZero(r.getMaxMatchScore())));

        long demandUpdated = 0;
        long patentUpdated = 0;
        long resultUpdated = 0;

        // 3) 逐需求进行匹配
        for (DemandEntity demand : demandList) {
            String demandField = demand.getField();
            String demandNecName = convertNecCodeToName(demandField, necNameMap);

            // 3.1 需求 vs 专利
            List<SupplyDemandMatchRequest> patentBatch = new ArrayList<>();
            for (PatentInfoEntity patent : patentList) {
                if (isFieldNecMatch(demandField, patent.getNec())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    PatentMatchDTO patentDTO = BeanUtil.toBean(patent, PatentMatchDTO.class);
                    patentDTO.setNec(convertNecCodeToName(patent.getNec(), necNameMap));

                    SupplyDemandMatchRequest req = new SupplyDemandMatchRequest();
                    req.setDemandType(DemandResponse.BIZ_CODE);
                    req.setDemandId(demand.getId());
                    req.setDemandContent(JSONUtil.toJsonStr(demandDTO));
                    req.setSupplyType(PatentInfoResponse.BIZ_CODE);
                    req.setSupplyId(patent.getId());
                    req.setSupplyContent(JSONUtil.toJsonStr(patentDTO));
                    patentBatch.add(req);

                    if (patentBatch.size() >= MATCH_BATCH_SIZE) {
                        List<SupplyDemandMatchResultEntity> matches = supplyDemandMatchResultService.matchBatch(patentBatch);
                        demandUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getDemandId,
                                demandCurrentMax, (id, score) -> {
                                    DemandEntity d = new DemandEntity();
                                    d.setId(id);
                                    d.setMaxMatchScore(score);
                                    return d;
                                }, demandService::updateBatchById);
                        patentUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getSupplyId,
                                patentCurrentMax, (id, score) -> {
                                    PatentInfoEntity p = new PatentInfoEntity();
                                    p.setId(id);
                                    p.setMaxMatchScore(score);
                                    return p;
                                }, patentInfoService::updateBatchById);
                        patentBatch.clear();
                    }
                }
            }
            if (!patentBatch.isEmpty()) {
                List<SupplyDemandMatchResultEntity> matches = supplyDemandMatchResultService.matchBatch(patentBatch);
                demandUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getDemandId,
                        demandCurrentMax, (id, score) -> {
                            DemandEntity d = new DemandEntity();
                            d.setId(id);
                            d.setMaxMatchScore(score);
                            return d;
                        }, demandService::updateBatchById);
                patentUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getSupplyId,
                        patentCurrentMax, (id, score) -> {
                            PatentInfoEntity p = new PatentInfoEntity();
                            p.setId(id);
                            p.setMaxMatchScore(score);
                            return p;
                        }, patentInfoService::updateBatchById);
            }

            // 3.2 需求 vs 成果
            List<SupplyDemandMatchRequest> resultBatch = new ArrayList<>();
            for (ResultEntity result : resultList) {
                if (isFieldNecMatch(demandField, result.getTechArea())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    ResultMatchDTO resultDTO = BeanUtil.toBean(result, ResultMatchDTO.class);
                    resultDTO.setNec(convertNecCodeToName(result.getTechArea(), necNameMap));

                    SupplyDemandMatchRequest req = new SupplyDemandMatchRequest();
                    req.setDemandType(DemandResponse.BIZ_CODE);
                    req.setDemandId(demand.getId());
                    req.setDemandContent(JSONUtil.toJsonStr(demandDTO));
                    req.setSupplyType(ResultResponse.BIZ_CODE);
                    req.setSupplyId(result.getId());
                    req.setSupplyContent(JSONUtil.toJsonStr(resultDTO));
                    resultBatch.add(req);

                    if (resultBatch.size() >= MATCH_BATCH_SIZE) {
                        List<SupplyDemandMatchResultEntity> matches = supplyDemandMatchResultService.matchBatch(resultBatch);
                        demandUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getDemandId,
                                demandCurrentMax, (id, score) -> {
                                    DemandEntity d = new DemandEntity();
                                    d.setId(id);
                                    d.setMaxMatchScore(score);
                                    return d;
                                }, demandService::updateBatchById);
                        resultUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getSupplyId,
                                resultCurrentMax, (id, score) -> {
                                    ResultEntity r = new ResultEntity();
                                    r.setId(id);
                                    r.setMaxMatchScore(score);
                                    return r;
                                }, resultService::updateBatchById);
                        resultBatch.clear();
                    }
                }
            }
            if (!resultBatch.isEmpty()) {
                List<SupplyDemandMatchResultEntity> matches = supplyDemandMatchResultService.matchBatch(resultBatch);
                demandUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getDemandId,
                        demandCurrentMax, (id, score) -> {
                            DemandEntity d = new DemandEntity();
                            d.setId(id);
                            d.setMaxMatchScore(score);
                            return d;
                        }, demandService::updateBatchById);
                resultUpdated += updateMaxScoreBatch(matches, SupplyDemandMatchResultEntity::getSupplyId,
                        resultCurrentMax, (id, score) -> {
                            ResultEntity r = new ResultEntity();
                            r.setId(id);
                            r.setMaxMatchScore(score);
                            return r;
                        }, resultService::updateBatchById);
            }
        }

        log.info("[MatchJob] demandMatch done: demandUpdated={}, patentUpdated={}, resultUpdated={}",
                demandUpdated, patentUpdated, resultUpdated);

        return true;
    }

    /* ================= 工具方法 ================= */

    private static long defaultZero(Long val) {
        return val == null ? 0L : val;
    }

    private <T> int updateMaxScoreBatch(List<SupplyDemandMatchResultEntity> matches,
                                        java.util.function.Function<SupplyDemandMatchResultEntity, Long> idGetter,
                                        Map<Long, Long> currentMax,
                                        java.util.function.BiFunction<Long, Long, T> entityBuilder,
                                        java.util.function.Consumer<List<T>> updater) {
        if (matches == null || matches.isEmpty()) {
            return 0;
        }
        Map<Long, Long> updated = new HashMap<>();
        for (SupplyDemandMatchResultEntity matchResult : matches) {
            Integer score = matchResult.getMatchScore();
            if (score == null) {
                continue;
            }
            Long id = idGetter.apply(matchResult);
            if (id == null) {
                continue;
            }
            long newScore = score.longValue();
            long current = currentMax.getOrDefault(id, 0L);
            if (newScore > current) {
                long existing = updated.getOrDefault(id, current);
                if (newScore > existing) {
                    updated.put(id, newScore);
                }
                currentMax.put(id, Math.max(current, newScore));
            }
        }
        if (updated.isEmpty()) {
            return 0;
        }
        List<T> toUpdate = updated.entrySet().stream()
                .map(e -> entityBuilder.apply(e.getKey(), e.getValue()))
                .toList();
        updater.accept(toUpdate);
        return toUpdate.size();
    }

    /**
     * NEC 匹配：a 与 b 均为分号分隔的 code 列表；若存在“b 的任一项以 a 的任一项为前缀”则匹配。
     */
    public boolean isFieldNecMatch(String a, String b) {
        if (StrUtil.isBlank(a) || StrUtil.isBlank(b)) return false;
        List<String> aList = Arrays.stream(a.split(";"))
                .map(String::trim).filter(StrUtil::isNotBlank).toList();
        List<String> bList = Arrays.stream(b.split(";"))
                .map(String::trim).filter(StrUtil::isNotBlank).toList();
        for (String av : aList) {
            for (String bv : bList) {
                if (bv.startsWith(av)) return true;
            }
        }
        return false;
    }

    /**
     * 把 NEC 代码串（; 分隔）转换为名称串（; 分隔），找不到码的保持原码。
     */
    private String convertNecCodeToName(String necCodes, Map<String, String> necNameMap) {
        if (StrUtil.isBlank(necCodes)) return necCodes;
        return Arrays.stream(necCodes.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .map(code -> necNameMap.getOrDefault(code, code))
                .collect(Collectors.joining(";"));
    }
}
