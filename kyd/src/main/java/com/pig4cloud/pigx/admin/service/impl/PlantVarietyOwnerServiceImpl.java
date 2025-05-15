package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PlantVarietyOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.PlantVarietyOwnerMapper;
import com.pig4cloud.pigx.admin.service.PlantVarietyOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 植物新品种权权利人信息
 *
 * @author pigx
 * @date 2025-05-15 10:56:56
 */
@Service
public class PlantVarietyOwnerServiceImpl extends ServiceImpl<PlantVarietyOwnerMapper, PlantVarietyOwnerEntity>
        implements PlantVarietyOwnerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPlantVarietyIds(List<Long> plantVarietyIds) {
        if (CollUtil.isEmpty(plantVarietyIds)) {
            return Boolean.TRUE;
        }
        return this.remove(Wrappers.<PlantVarietyOwnerEntity>lambdaQuery()
                .in(PlantVarietyOwnerEntity::getPlantVarietyId, plantVarietyIds));
    }
}
