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
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final DemandServiceImpl demandService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;
    private final DimEcService dimEcService;

    /**
     * 计算匹配并批量更新：需求/专利/成果 的 maxMatchScore
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

        // 2.1 当前库中的最高分快照
        Map<Long, Long> demandCurrentMax = demandList.stream()
                .collect(Collectors.toMap(DemandEntity::getId, d -> defaultZero(d.getMaxMatchScore())));
        Map<Long, Long> patentCurrentMax = patentList.stream()
                .collect(Collectors.toMap(PatentInfoEntity::getId, p -> defaultZero(p.getMaxMatchScore())));
        Map<Long, Long> resultCurrentMax = resultList.stream()
                .collect(Collectors.toMap(ResultEntity::getId, r -> defaultZero(r.getMaxMatchScore())));

        // 2.2 newMax 容器（初始为当前值）
        Map<Long, Long> demandNewMax = new HashMap<>(demandCurrentMax);
        Map<Long, Long> patentNewMax = new HashMap<>(patentCurrentMax);
        Map<Long, Long> resultNewMax = new HashMap<>(resultCurrentMax);

        // 3) 逐需求进行匹配
        for (DemandEntity demand : demandList) {
            String demandField = demand.getField();
            String demandNecName = convertNecCodeToName(demandField, necNameMap);

            // 3.1 需求 vs 专利
            for (PatentInfoEntity patent : patentList) {
                if (isFieldNecMatch(demandField, patent.getNec())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    PatentMatchDTO patentDTO = BeanUtil.toBean(patent, PatentMatchDTO.class);
                    patentDTO.setNec(convertNecCodeToName(patent.getNec(), necNameMap));

                    SupplyDemandMatchResultEntity matchResult = supplyDemandMatchResultService.match(
                            DemandResponse.BIZ_CODE, demand.getId(), JSONUtil.toJsonStr(demandDTO),
                            PatentInfoResponse.BIZ_CODE, patent.getId(), JSONUtil.toJsonStr(patentDTO)
                    );

                    Integer score = matchResult == null ? null : matchResult.getMatchScore();
                    if (score != null) {
                        long s = score.longValue();
                        // 专利侧 newMax
                        patentNewMax.merge(patent.getId(), s, Math::max);
                        // 需求侧 newMax（★关键补充）
                        demandNewMax.merge(demand.getId(), s, Math::max);
                    }
                }
            }

            // 3.2 需求 vs 成果
            for (ResultEntity result : resultList) {
                if (isFieldNecMatch(demandField, result.getTechArea())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    ResultMatchDTO resultDTO = BeanUtil.toBean(result, ResultMatchDTO.class);
                    resultDTO.setNec(convertNecCodeToName(result.getTechArea(), necNameMap));

                    SupplyDemandMatchResultEntity matchResult = supplyDemandMatchResultService.match(
                            DemandResponse.BIZ_CODE, demand.getId(), JSONUtil.toJsonStr(demandDTO),
                            ResultResponse.BIZ_CODE, result.getId(), JSONUtil.toJsonStr(resultDTO)
                    );

                    Integer score = matchResult == null ? null : matchResult.getMatchScore();
                    if (score != null) {
                        long s = score.longValue();
                        // 成果侧 newMax
                        resultNewMax.merge(result.getId(), s, Math::max);
                        // 需求侧 newMax（★关键补充）
                        demandNewMax.merge(demand.getId(), s, Math::max);
                    }
                }
            }
        }

        // 4) 仅更新“更高”的 maxMatchScore
        List<DemandEntity> demandToUpdate = demandNewMax.entrySet().stream()
                .filter(e -> e.getValue() > demandCurrentMax.getOrDefault(e.getKey(), 0L))
                .map(e -> {
                    DemandEntity d = new DemandEntity();
                    d.setId(e.getKey());
                    d.setMaxMatchScore(e.getValue());
                    return d;
                })
                .toList();

        List<PatentInfoEntity> patentToUpdate = patentNewMax.entrySet().stream()
                .filter(e -> e.getValue() > patentCurrentMax.getOrDefault(e.getKey(), 0L))
                .map(e -> {
                    PatentInfoEntity p = new PatentInfoEntity();
                    p.setId(e.getKey());
                    p.setMaxMatchScore(e.getValue());
                    return p;
                })
                .toList();

        List<ResultEntity> resultToUpdate = resultNewMax.entrySet().stream()
                .filter(e -> e.getValue() > resultCurrentMax.getOrDefault(e.getKey(), 0L))
                .map(e -> {
                    ResultEntity r = new ResultEntity();
                    r.setId(e.getKey());
                    r.setMaxMatchScore(e.getValue());
                    return r;
                })
                .toList();

        if (!demandToUpdate.isEmpty()) {
            demandService.updateBatchById(demandToUpdate);
        }
        if (!patentToUpdate.isEmpty()) {
            patentInfoService.updateBatchById(patentToUpdate);
        }
        if (!resultToUpdate.isEmpty()) {
            resultService.updateBatchById(resultToUpdate);
        }

        return true;
    }

    /* ================= 工具方法 ================= */

    private static long defaultZero(Long val) {
        return val == null ? 0L : val;
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
