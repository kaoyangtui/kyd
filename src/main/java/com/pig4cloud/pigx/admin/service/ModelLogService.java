package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.ModelAliEnum;
import com.pig4cloud.pigx.admin.constants.ModelBizNameEnum;
import com.pig4cloud.pigx.admin.constants.ModelVolcEnum;
import com.pig4cloud.pigx.admin.entity.ModelLogEntity;

import java.util.List;

public interface ModelLogService extends IService<ModelLogEntity> {

    ModelLogEntity modelAliCall(ModelBizNameEnum bizNameEnum, ModelAliEnum model, String code, Long userId, String inputContent);

    ModelLogEntity modelVolcCall(ModelBizNameEnum bizNameEnum, ModelVolcEnum model, String code, Long userId, String inputContent);

    ModelLogEntity modelVolcCall(ModelBizNameEnum bizNameEnum, ModelVolcEnum model, String code, Long userId, String inputContent, List<String> inputImg);
}