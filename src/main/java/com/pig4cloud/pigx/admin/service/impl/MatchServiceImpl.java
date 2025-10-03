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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// 其它 import 保持不变

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final DemandServiceImpl demandService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;
    private final DimEcService dimEcService;

    /**
     * 计算匹配并批量更新专利/成果的 maxMatchScore
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean demandMatch() {
        // 1. 一次性查出所有 NEC code -> name
        Map<String, String> necNameMap = dimEcService.lambdaQuery()
                .list()
                .stream()
                .filter(e -> StrUtil.isNotBlank(e.getCode()))
                .collect(Collectors.toMap(
                        DimEcEntity::getCode,
                        DimEcEntity::getName,
                        // 遇到重复 code 取第一个
                        (a, b) -> a
                ));

        // 2. 获取需求、专利、成果（含已有 maxMatchScore）
        List<DemandEntity> demandList = demandService.lambdaQuery()
                .eq(DemandEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();

        List<PatentInfoEntity> patentList = patentInfoService.lambdaQuery()
                .eq(PatentInfoEntity::getStatusCode, PatentStatusEnum.VALID.getCode())
                .eq(PatentInfoEntity::getMergeFlag, "1")
                .in(PatentInfoEntity::getPatType, PatentTypeEnum.INVENTION.getCode(), PatentTypeEnum.UTILITY_MODEL.getCode())
                .list();

        List<ResultEntity> resultList = resultService.lambdaQuery()
                .eq(ResultEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();

        // 2.1 把当前库里的 max 分数记下来，后续只收集“提升”的
        Map<Long, Long> patentCurrentMax = patentList.stream()
                .collect(Collectors.toMap(
                        PatentInfoEntity::getId,
                        p -> defaultZero(p.getMaxMatchScore())
                ));
        Map<Long, Long> resultCurrentMax = resultList.stream()
                .collect(Collectors.toMap(
                        ResultEntity::getId,
                        r -> defaultZero(r.getMaxMatchScore())
                ));

        // 2.2 用于累计新的（更高的）max
        Map<Long, Long> patentNewMax = new java.util.HashMap<>(patentCurrentMax);
        Map<Long, Long> resultNewMax = new java.util.HashMap<>(resultCurrentMax);

        // 3. 匹配（过程中只做计算与入结果表；同时刷新内存中的 newMax）
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

                    // 刷新“内存中的最高分”（仅当更高）
                    Integer score = matchResult != null ? matchResult.getMatchScore() : null;
                    if (score != null) {
                        long s = score.longValue();
                        Long cur = patentNewMax.getOrDefault(patent.getId(), 0L);
                        if (s > cur) {
                            patentNewMax.put(patent.getId(), s);
                        }
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

                    Integer score = matchResult != null ? matchResult.getMatchScore() : null;
                    if (score != null) {
                        long s = score.longValue();
                        Long cur = resultNewMax.getOrDefault(result.getId(), 0L);
                        if (s > cur) {
                            resultNewMax.put(result.getId(), s);
                        }
                    }
                }
            }
        }

        // 4. 汇总出“需要更新”的对象，批量落库（仅更新更高的）
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

        if (!patentToUpdate.isEmpty()) {
            // MyBatis-Plus: 仅非空字段会更新；只填了 id + maxMatchScore
            patentInfoService.updateBatchById(patentToUpdate);
        }
        if (!resultToUpdate.isEmpty()) {
            resultService.updateBatchById(resultToUpdate);
        }

        return true;
    }

    private static long defaultZero(Long val) {
        return val == null ? 0L : val;
    }

    public boolean isFieldNecMatch(String a, String b) {
        if (StrUtil.isBlank(a) || StrUtil.isBlank(b)) {
            return false;
        }
        // 拆成List并去空格
        List<String> aList = Arrays.stream(a.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList();
        List<String> bList = Arrays.stream(b.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList();
        // 任意field是任意nec的前缀
        for (String aItem : aList) {
            for (String bItem : bList) {
                if (bItem.startsWith(aItem)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String convertNecCodeToName(String necCodes, Map<String, String> necNameMap) {
        if (StrUtil.isBlank(necCodes)) {
            return necCodes;
        }
        return Arrays.stream(necCodes.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .map(code -> necNameMap.getOrDefault(code, code))
                .collect(Collectors.joining(";"));
    }
}
