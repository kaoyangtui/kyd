package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PlantVarietyBreederEntity;

import java.util.List;

public interface PlantVarietyBreederService extends IService<PlantVarietyBreederEntity> {

    Boolean removeByPlantVarietyIds(List<Long> ids);
}