package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.ModelBizNameEnum;
import com.pig4cloud.pigx.admin.constants.ModelVolcEnum;
import com.pig4cloud.pigx.admin.dto.match.SupplyDemandMatchRequest;
import com.pig4cloud.pigx.admin.entity.ModelLogEntity;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;
import com.pig4cloud.pigx.admin.mapper.SupplyDemandMatchResultMapper;
import com.pig4cloud.pigx.admin.prompt.MatchPrompt;
import com.pig4cloud.pigx.admin.service.ModelLogService;
import com.pig4cloud.pigx.admin.service.SupplyDemandMatchResultService;
import com.pig4cloud.pigx.admin.utils.ModelStatusEnum;
import com.pig4cloud.pigx.admin.utils.ModelVolcUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 供需匹配结果表
 *
 * @author pigx
 * @date 2025-07-31 16:07:15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SupplyDemandMatchResultServiceImpl extends ServiceImpl<SupplyDemandMatchResultMapper, SupplyDemandMatchResultEntity> implements SupplyDemandMatchResultService {

    private final ModelLogService modelLogService;

    @Override
    public SupplyDemandMatchResultEntity match(String demandType,
                                               Long demandId,
                                               String demandContent,
                                               String supplyType,
                                               Long supplyId,
                                               String supplyContent) {
        // 1. 查重（按需求/供给类型、编码、状态）
        SupplyDemandMatchResultEntity existed = this.lambdaQuery()
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getDemandId, demandId)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getSupplyId, supplyId)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .last("limit 1")
                .one();
        if (existed != null) {
            return existed;
        }

        // 2. 未查到则继续走模型流程
        String code = IdUtil.getSnowflakeNextIdStr();
        String content = StrUtil.format(MatchPrompt.VALUE, supplyContent, demandContent);

        ModelLogEntity modelLog = modelLogService.modelVolcCall(
                ModelBizNameEnum.MATCH, ModelVolcEnum.DOUBAO_SEED_1_6, code, 0L, content
        );

        if (modelLog == null || StrUtil.isBlank(modelLog.getOutputContent())) {
            log.warn("[Match] model output is empty, code={}, demandId={}, supplyId={}", code, demandId, supplyId);
            return null;
        }

        SupplyDemandMatchResultEntity entity = parseMatchResult(modelLog.getOutputContent());
        if (entity == null) {
            log.warn("[Match] model output parse failed, code={}, demandId={}, supplyId={}", code, demandId, supplyId);
            return null;
        }
        entity.setCode(code);
        entity.setMatchDate(LocalDate.now());
        entity.setDemandId(demandId);
        entity.setDemandType(demandType);
        entity.setDemandContent(demandContent);
        entity.setSupplyId(supplyId);
        entity.setSupplyType(supplyType);
        entity.setSupplyContent(supplyContent);
        entity.setMatchStatus(ModelStatusEnum.SUCCESS.getValue());
        entity.setMatchReasoning(modelLog.getInferenceContent());

        this.save(entity);
        return entity;
    }

    @Override
    public List<SupplyDemandMatchResultEntity> matchBatch(List<SupplyDemandMatchRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            log.info("[MatchBatch] empty requests");
            return List.of();
        }

        List<SupplyDemandMatchResultEntity> outputs = new ArrayList<>();
        List<SupplyDemandMatchRequest> pending = new ArrayList<>();

        for (SupplyDemandMatchRequest req : requests) {
            if (req == null) {
                continue;
            }
            SupplyDemandMatchResultEntity existed = this.lambdaQuery()
                    .eq(SupplyDemandMatchResultEntity::getDemandType, req.getDemandType())
                    .eq(SupplyDemandMatchResultEntity::getDemandId, req.getDemandId())
                    .eq(SupplyDemandMatchResultEntity::getSupplyType, req.getSupplyType())
                    .eq(SupplyDemandMatchResultEntity::getSupplyId, req.getSupplyId())
                    .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                    .last("limit 1")
                    .one();
            if (existed != null) {
                outputs.add(existed);
                continue;
            }
            pending.add(req);
        }

        if (pending.isEmpty()) {
            log.info("[MatchBatch] all requests already matched, total={}", requests.size());
            return outputs;
        }

        log.info("[MatchBatch] start batch match: total={}, pending={}", requests.size(), pending.size());

        List<String> prompts = new ArrayList<>(pending.size());
        List<String> codes = new ArrayList<>(pending.size());
        for (SupplyDemandMatchRequest req : pending) {
            codes.add(IdUtil.getSnowflakeNextIdStr());
            prompts.add(StrUtil.format(MatchPrompt.VALUE, req.getSupplyContent(), req.getDemandContent()));
        }

        Map<String, Object> batchResult = ModelVolcUtils.modelCallBatch(
                prompts,
                ModelVolcEnum.BATCH_MATCH.getValue(),
                prompts.size(),
                Duration.ofMinutes(10),
                null
        );

        List<Map<String, Object>> results = MapUtil.get(batchResult, "results", List.class);
        List<Map<String, Object>> failures = MapUtil.get(batchResult, "failures", List.class);

        if (results == null) {
            results = List.of();
        }
        if (failures == null) {
            failures = List.of();
        }

        if (results.isEmpty() && !failures.isEmpty()) {
            log.warn("[MatchBatch] batch failed, fallback to single, size={}", pending.size());
            for (int i = 0; i < pending.size(); i++) {
                SupplyDemandMatchRequest req = pending.get(i);
                SupplyDemandMatchResultEntity one = match(
                        req.getDemandType(),
                        req.getDemandId(),
                        req.getDemandContent(),
                        req.getSupplyType(),
                        req.getSupplyId(),
                        req.getSupplyContent()
                );
                if (one != null) {
                    outputs.add(one);
                }
            }
            return outputs;
        }

        log.info("[MatchBatch] batch response: success={}, failure={}", results.size(), failures.size());

        Map<Integer, String> outputMap = new HashMap<>();
        for (Map<String, Object> item : results) {
            Integer idx = MapUtil.getInt(item, "index");
            String text = MapUtil.getStr(item, "result");
            if (idx != null) {
                outputMap.put(idx, text);
            }
        }

        for (int i = 0; i < pending.size(); i++) {
            SupplyDemandMatchRequest req = pending.get(i);
            String output = outputMap.get(i);
            if (StrUtil.isBlank(output)) {
                log.warn("[MatchBatch] empty output, index={}, demandId={}, supplyId={}",
                        i, req.getDemandId(), req.getSupplyId());
                continue;
            }

            SupplyDemandMatchResultEntity entity = parseMatchResult(output);
            if (entity == null) {
                continue;
            }

            String code = codes.get(i);
            entity.setCode(code);
            entity.setMatchDate(LocalDate.now());
            entity.setDemandId(req.getDemandId());
            entity.setDemandType(req.getDemandType());
            entity.setDemandContent(req.getDemandContent());
            entity.setSupplyId(req.getSupplyId());
            entity.setSupplyType(req.getSupplyType());
            entity.setSupplyContent(req.getSupplyContent());
            entity.setMatchStatus(ModelStatusEnum.SUCCESS.getValue());
            entity.setMatchReasoning(null);

            this.save(entity);
            outputs.add(entity);

            ModelLogEntity logEntity = new ModelLogEntity();
            logEntity.setCode(code);
            logEntity.setUserId(0L);
            logEntity.setModelType(ModelVolcEnum.BATCH_MATCH.getValue());
            logEntity.setBizName(ModelBizNameEnum.MATCH.getValue());
            logEntity.setInputContent(prompts.get(i));
            logEntity.setOutputContent(output);
            logEntity.setStatus(ModelStatusEnum.SUCCESS.getValue());
            logEntity.setJobStartTime(LocalDateTime.now());
            logEntity.setJobEndTime(LocalDateTime.now());
            logEntity.setJobExeTime(0);
            modelLogService.save(logEntity);
        }

        if (!failures.isEmpty()) {
            log.warn("[MatchBatch] failures recorded, count={}", failures.size());
            for (Map<String, Object> item : failures) {
                Integer idx = MapUtil.getInt(item, "index");
                if (idx == null || idx < 0 || idx >= pending.size()) {
                    continue;
                }
                ModelLogEntity logEntity = new ModelLogEntity();
                logEntity.setCode(codes.get(idx));
                logEntity.setUserId(0L);
                logEntity.setModelType(ModelVolcEnum.BATCH_MATCH.getValue());
                logEntity.setBizName(ModelBizNameEnum.MATCH.getValue());
                logEntity.setInputContent(prompts.get(idx));
                logEntity.setStatus(ModelStatusEnum.FAILED.getValue());
                logEntity.setErrorMessage(MapUtil.getStr(item, "error"));
                logEntity.setJobStartTime(LocalDateTime.now());
                logEntity.setJobEndTime(LocalDateTime.now());
                logEntity.setJobExeTime(0);
                modelLogService.save(logEntity);
            }
        }

        return outputs;
    }

    @Override
    public IPage<SupplyDemandMatchResultEntity> pageMatchByDemand(String demandType,
                                                                  Long demandId,
                                                                  String supplyType,
                                                                  IPage page) {
        return this.lambdaQuery()
                .select(SupplyDemandMatchResultEntity::getSupplyId, SupplyDemandMatchResultEntity::getMatchScore)
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getDemandId, demandId)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .orderByDesc(SupplyDemandMatchResultEntity::getMatchScore)
                .page(page);
    }

    @Override
    public IPage<SupplyDemandMatchResultEntity> pageMatchBySupply(String demandType,
                                                                  Long supplyId,
                                                                  String supplyType,
                                                                  IPage page) {
        return this.lambdaQuery()
                .select(SupplyDemandMatchResultEntity::getDemandId, SupplyDemandMatchResultEntity::getMatchScore)
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getSupplyId, supplyId)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .orderByDesc(SupplyDemandMatchResultEntity::getMatchScore)
                .page(page);
    }

    @Override
    public long countDistinctSupplyByDemand(String demandType,
                                            Long demandId,
                                            String supplyType) {
        return countDistinct("supply_id", demandType, demandId, supplyType, true);
    }

    @Override
    public long countDistinctDemandBySupply(String demandType,
                                            Long supplyId,
                                            String supplyType) {
        return countDistinct("demand_id", demandType, supplyId, supplyType, false);
    }

    private long countDistinct(String column,
                               String demandType,
                               Long id,
                               String supplyType,
                               boolean byDemand) {
        QueryWrapper<SupplyDemandMatchResultEntity> wrapper = new QueryWrapper<>();
        wrapper.select("count(distinct " + column + ") as cnt")
                .eq("demand_type", demandType)
                .eq("supply_type", supplyType)
                .eq("match_status", ModelStatusEnum.SUCCESS.getValue());
        if (byDemand) {
            wrapper.eq("demand_id", id);
        } else {
            wrapper.eq("supply_id", id);
        }
        List<Map<String, Object>> rows = this.baseMapper.selectMaps(wrapper);
        if (rows.isEmpty() || rows.get(0).get("cnt") == null) {
            return 0L;
        }
        return ((Number) rows.get(0).get("cnt")).longValue();
    }

    private SupplyDemandMatchResultEntity parseMatchResult(String output) {
        try {
            JSONObject obj = JSONUtil.parseObj(output);
            SupplyDemandMatchResultEntity entity = new SupplyDemandMatchResultEntity();
            Integer matchScore = getInt(obj, "matchScore", "match_score");
            entity.setMatchScore(matchScore);
            entity.setMatchResult(getStr(obj, "matchResult", "match_result"));
            entity.setSupplySummary(getStr(obj, "supplySummary", "supply_summary"));
            entity.setDemandSummary(getStr(obj, "demandSummary", "demand_summary"));
            entity.setRelatedKeywords(normalizeKeywords(obj.get("relatedKeywords"), obj.get("related_keywords")));
            entity.setAdvice(getStr(obj, "advice"));
            return entity;
        } catch (Exception e) {
            log.warn("[Match] parse model output failed: {}", e.getMessage());
            return null;
        }
    }

    private Integer getInt(JSONObject obj, String... keys) {
        for (String key : keys) {
            Integer val = obj.getInt(key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    private String getStr(JSONObject obj, String... keys) {
        for (String key : keys) {
            String val = obj.getStr(key);
            if (StrUtil.isNotBlank(val)) {
                return val;
            }
        }
        return null;
    }

    private String normalizeKeywords(Object primary, Object fallback) {
        Object raw = primary != null ? primary : fallback;
        if (raw == null) {
            return null;
        }
        if (raw instanceof JSONArray) {
            return ((JSONArray) raw).stream()
                    .map(Object::toString)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.joining(";"));
        }
        if (raw instanceof Iterable<?> iterable) {
            List<String> parts = new java.util.ArrayList<>();
            for (Object item : iterable) {
                if (item != null && StrUtil.isNotBlank(item.toString())) {
                    parts.add(item.toString());
                }
            }
            return parts.isEmpty() ? null : String.join(";", parts);
        }
        String text = raw.toString();
        if (StrUtil.isBlank(text)) {
            return null;
        }
        if (text.startsWith("[") && text.endsWith("]")) {
            try {
                JSONArray arr = JSONUtil.parseArray(text);
                return arr.stream()
                        .map(Object::toString)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.joining(";"));
            } catch (Exception ignored) {
                return text;
            }
        }
        return text;
    }

    @Override
    public List<SupplyDemandMatchResultEntity> getMatchByDemand(String demandType,
                                                              Long demandId,
                                                              String supplyType) {
        return this.lambdaQuery()
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getDemandId, demandId)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .orderByDesc(SupplyDemandMatchResultEntity::getMatchScore)
                .list();
    }

    @Override
    public List<SupplyDemandMatchResultEntity> getMatchBySupply(String demandType,
                                                                Long supplyId,
                                                                String supplyType) {
        return this.lambdaQuery()
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getSupplyId, supplyId)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .orderByDesc(SupplyDemandMatchResultEntity::getMatchScore)
                .list();
    }

    @Override
    public List<Long> getMatchId(String supplyType) {
        return this.lambdaQuery()
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue())
                .orderByDesc(SupplyDemandMatchResultEntity::getMatchScore)
                .list()
                .stream()
                .map(SupplyDemandMatchResultEntity::getSupplyId)
                .collect(Collectors.toList());
    }

}
