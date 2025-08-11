package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.PatentStatusEnum;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.match.DemandMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.PatentMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.ResultMatchDTO;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DimEcEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final DemandServiceImpl demandService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;
    private final DimEcService dimEcService;

    @Override
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

        // 2. 获取需求、专利、成果
        List<DemandEntity> demandList = demandService.lambdaQuery()
                .eq(DemandEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();
        List<PatentInfoEntity> patentList = patentInfoService.lambdaQuery()
                .eq(PatentInfoEntity::getStatusCode, PatentStatusEnum.VALID.getCode())
                .list();
        List<ResultEntity> resultList = resultService.lambdaQuery()
                .eq(ResultEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .list();

        // 3. 这里就可以直接用 necNameMap 转换，不再查数据库
        for (DemandEntity demand : demandList) {
            String demandField = demand.getField();
            String demandNecName = convertNecCodeToName(demandField, necNameMap);

            for (PatentInfoEntity patent : patentList) {
                if (isFieldNecMatch(demandField, patent.getNec())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    PatentMatchDTO patentDTO = BeanUtil.toBean(patent, PatentMatchDTO.class);
                    patentDTO.setNec(convertNecCodeToName(patent.getNec(), necNameMap));

                    supplyDemandMatchResultService.match(
                            DemandResponse.BIZ_CODE, demand.getId(), JSONUtil.toJsonStr(demandDTO),
                            PatentInfoResponse.BIZ_CODE, patent.getId(), JSONUtil.toJsonStr(patentDTO)
                    );
                }
            }

            for (ResultEntity result : resultList) {
                if (isFieldNecMatch(demandField, result.getTechArea())) {
                    DemandMatchDTO demandDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    demandDTO.setNec(demandNecName);

                    ResultMatchDTO resultDTO = BeanUtil.toBean(result, ResultMatchDTO.class);
                    resultDTO.setNec(convertNecCodeToName(result.getTechArea(), necNameMap));

                    supplyDemandMatchResultService.match(
                            DemandResponse.BIZ_CODE, demand.getId(), JSONUtil.toJsonStr(demandDTO),
                            ResultResponse.BIZ_CODE, result.getId(), JSONUtil.toJsonStr(resultDTO)
                    );
                }
            }
        }
        return true;
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
