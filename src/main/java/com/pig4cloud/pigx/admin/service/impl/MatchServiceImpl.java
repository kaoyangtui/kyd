package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.PatentStatusEnum;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.match.DemandMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.PatentMatchDTO;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;
import com.pig4cloud.pigx.admin.service.MatchService;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.service.SupplyDemandMatchResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final DemandServiceImpl demandService;
    private final PatentInfoService patentInfoService;
    private final SupplyDemandMatchResultService supplyDemandMatchResultService;

    @Override
    public Boolean demandMatch() {
        List<DemandEntity> demandList = demandService.lambdaQuery()
                .eq(DemandEntity::getFlowStatus, FlowStatusEnum.COMPLETE.getValue())
                .list();
        List<PatentInfoEntity> patentList = patentInfoService.lambdaQuery()
                .eq(PatentInfoEntity::getStatusCode, PatentStatusEnum.VALID.getCode())
                .list();
        for (DemandEntity demand : demandList) {
            String demandField = demand.getField();
            for (PatentInfoEntity patent : patentList) {
                String nec = patent.getNec();
                if (isFieldNecMatch(demandField, nec)) {
                    DemandMatchDTO demandMatchDTO = BeanUtil.toBean(demand, DemandMatchDTO.class);
                    PatentMatchDTO patentMatchDTO = BeanUtil.toBean(patent, PatentMatchDTO.class);
                    supplyDemandMatchResultService.match(DemandResponse.BIZ_CODE,
                            demand.getCode(),
                            JSONUtil.toJsonStr(demandMatchDTO),
                            PatentInfoResponse.BIZ_CODE,
                            patent.getPid(),
                            JSONUtil.toJsonStr(patentMatchDTO)
                    );
                }
            }
        }
        return null;
    }

    public boolean isFieldNecMatch(String demandField, String patentNec) {
        if (StrUtil.isBlank(demandField) || StrUtil.isBlank(patentNec)) {
            return false;
        }
        // 拆成List并去空格
        List<String> fieldList = Arrays.stream(demandField.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList();
        List<String> necList = Arrays.stream(patentNec.split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList();
        // 任意field是任意nec的前缀
        for (String field : fieldList) {
            for (String nec : necList) {
                if (nec.startsWith(field)) {
                    return true;
                }
            }
        }
        return false;
    }
}
