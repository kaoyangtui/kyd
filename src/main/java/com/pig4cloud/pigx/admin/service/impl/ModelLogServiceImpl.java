package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.ModelAliEnum;
import com.pig4cloud.pigx.admin.constants.ModelBizNameEnum;
import com.pig4cloud.pigx.admin.constants.ModelVolcEnum;
import com.pig4cloud.pigx.admin.entity.ModelLogEntity;
import com.pig4cloud.pigx.admin.mapper.ModelLogMapper;
import com.pig4cloud.pigx.admin.service.ModelLogService;
import com.pig4cloud.pigx.admin.utils.ModelAliUtils;
import com.pig4cloud.pigx.admin.utils.ModelStatusEnum;
import com.pig4cloud.pigx.admin.utils.ModelVolcUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 大模型调用日志表
 *
 * @author pigx
 * @date 2025-07-31 15:47:16
 */
@Slf4j
@Service
public class ModelLogServiceImpl extends ServiceImpl<ModelLogMapper, ModelLogEntity> implements ModelLogService {


    @Override
    public String modelAliCall(ModelBizNameEnum modelBizNameEnum, ModelAliEnum model, String code, Long userId, String inputContent) {
        return modelCall(modelBizNameEnum, model.getValue(), code, userId, inputContent, null, "ali");
    }

    @Override
    public String modelVolcCall(ModelBizNameEnum modelBizNameEnum, ModelVolcEnum model, String code, Long userId, String inputContent) {
        return modelVolcCall(modelBizNameEnum, model, code, userId, inputContent, null);
    }

    @Override
    public String modelVolcCall(ModelBizNameEnum modelBizNameEnum, ModelVolcEnum model, String code, Long userId, String inputContent, List<String> inputImg) {
        return modelCall(modelBizNameEnum, model.getValue(), code, userId, inputContent, inputImg, "volc");
    }

    @Nullable
    private String modelCall(ModelBizNameEnum modelBizNameEnum, String model, String code, Long userId, String inputContent, List<String> inputImg, String source) {
        try {
            LocalDateTime startTime = LocalDateTime.now();
            Map<String, Object> map;
            if ("ali".equals(source)) {
                map = ModelAliUtils.modelCall(inputContent, model);
            } else if ("volc".equals(source)) {
                if (null != inputImg && !inputImg.isEmpty()) {
                    map = ModelVolcUtils.modelCallWithImg(inputContent, inputImg, model);
                } else {
                    map = ModelVolcUtils.modelCall(inputContent, model);
                }
            } else {
                throw new RuntimeException("source is not ali or volc");
            }
            LocalDateTime endTime = LocalDateTime.now();
            long jobExeTime = Duration.between(startTime, endTime).getSeconds();
            log.info("{} 执行耗时: {} s", modelBizNameEnum.getValue(), jobExeTime);
            String result = MapUtil.getStr(map, "result")
                    .replaceAll("(?s)^.*?([\\[{].*[\\]}]).*$", "$1");
            String reasoning = MapUtil.getStr(map, "reasoning");
            Long totalInputTokens = MapUtil.getLong(map, "total_input_tokens");
            Long totalOutputTokens = MapUtil.getLong(map, "total_output_tokens");
            ModelLogEntity modelLogEntity = new ModelLogEntity();
            modelLogEntity.setCode(code);
            modelLogEntity.setUserId(userId);
            modelLogEntity.setModelType(model);
            modelLogEntity.setBizName(modelBizNameEnum.getValue());
            modelLogEntity.setInputContent(inputContent);
            modelLogEntity.setOutputContent(result);
            modelLogEntity.setInferenceContent(reasoning);
            modelLogEntity.setStatus(ModelStatusEnum.SUCCESS.getValue());
            modelLogEntity.setJobStartTime(startTime);
            modelLogEntity.setJobEndTime(endTime);
            modelLogEntity.setJobExeTime(Math.toIntExact(jobExeTime));
            modelLogEntity.setInputTokens(totalInputTokens);
            modelLogEntity.setOutputTokens(totalOutputTokens);
            this.save(modelLogEntity);
            return result;
        } catch (Exception exception) {
            log.error("{} 模型调用失败", modelBizNameEnum.getValue(), exception);
            return null;
        }
    }
}