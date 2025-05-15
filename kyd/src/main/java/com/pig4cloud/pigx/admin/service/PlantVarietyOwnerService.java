package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PlantVarietyOwnerEntity;

import java.util.List;

public interface PlantVarietyOwnerService extends IService<PlantVarietyOwnerEntity> {

    Boolean removeByPlantVarietyIds(List<Long> ids);
}