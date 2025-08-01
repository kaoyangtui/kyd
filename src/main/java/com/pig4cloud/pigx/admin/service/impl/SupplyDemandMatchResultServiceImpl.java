package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.ModelBizNameEnum;
import com.pig4cloud.pigx.admin.constants.ModelVolcEnum;
import com.pig4cloud.pigx.admin.entity.ModelLogEntity;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;
import com.pig4cloud.pigx.admin.mapper.SupplyDemandMatchResultMapper;
import com.pig4cloud.pigx.admin.prompt.MatchPrompt;
import com.pig4cloud.pigx.admin.service.ModelLogService;
import com.pig4cloud.pigx.admin.service.SupplyDemandMatchResultService;
import com.pig4cloud.pigx.admin.utils.ModelStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public SupplyDemandMatchResultEntity match(String demandType, String demandCode, String demandContent, String supplyType, String supplyCode, String supplyContent) {
        // 1. 查重（按需求/供给类型、编码、状态）
        SupplyDemandMatchResultEntity existed = this.lambdaQuery()
                .eq(SupplyDemandMatchResultEntity::getDemandType, demandType)
                .eq(SupplyDemandMatchResultEntity::getDemandCode, demandCode)
                .eq(SupplyDemandMatchResultEntity::getSupplyType, supplyType)
                .eq(SupplyDemandMatchResultEntity::getSupplyCode, supplyCode)
                .eq(SupplyDemandMatchResultEntity::getMatchStatus, ModelStatusEnum.SUCCESS.getValue()) // 只查已成功匹配的
                .last("limit 1")
                .one();
        if (existed != null) {
            return existed;
        }

        // 2. 未查到则继续走模型流程
        String code = IdUtil.getSnowflakeNextIdStr();
        String content = StrUtil.format(MatchPrompt.VALUE, supplyContent, demandContent);

        ModelLogEntity modelLog = modelLogService.modelVolcCall(
                ModelBizNameEnum.MATCH, ModelVolcEnum.DEEP_SEEK_R1, code, 0L, content
        );

        SupplyDemandMatchResultEntity entity = JSONUtil.toBean(modelLog.getOutputContent(), SupplyDemandMatchResultEntity.class);
        entity.setCode(code);
        entity.setDemandCode(demandCode);
        entity.setDemandType(demandType);
        entity.setDemandContent(demandContent);
        entity.setSupplyCode(supplyCode);
        entity.setSupplyType(supplyType);
        entity.setSupplyContent(supplyContent);
        entity.setMatchStatus(ModelStatusEnum.SUCCESS.getValue());
        entity.setMatchReasoning(modelLog.getInferenceContent());

        this.save(entity);
        return entity;
    }


}