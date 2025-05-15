package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PlantVarietyBreederEntity;
import com.pig4cloud.pigx.admin.mapper.PlantVarietyBreederMapper;
import com.pig4cloud.pigx.admin.service.PlantVarietyBreederService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 植物新品种权校内培育人信息
 *
 * @author pigx
 * @date 2025-05-15 10:56:23
 */
@Service
public class PlantVarietyBreederServiceImpl extends ServiceImpl<PlantVarietyBreederMapper, PlantVarietyBreederEntity>
        implements PlantVarietyBreederService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPlantVarietyIds(List<Long> plantVarietyIds) {
        if (CollUtil.isEmpty(plantVarietyIds)) {
            return Boolean.TRUE;
        }
        return this.remove(Wrappers.<PlantVarietyBreederEntity>lambdaQuery()
                .in(PlantVarietyBreederEntity::getPlantVarietyId, plantVarietyIds));
    }
}
