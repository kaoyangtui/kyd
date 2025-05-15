package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PlantVarietyBreederEntity;
import com.pig4cloud.pigx.admin.entity.PlantVarietyEntity;
import com.pig4cloud.pigx.admin.entity.PlantVarietyOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.PlantVarietyMapper;
import com.pig4cloud.pigx.admin.service.PlantVarietyBreederService;
import com.pig4cloud.pigx.admin.service.PlantVarietyOwnerService;
import com.pig4cloud.pigx.admin.service.PlantVarietyService;
import com.pig4cloud.pigx.admin.vo.plantVariety.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 植物新品种权登记信息
 *
 * @author pigx
 * @date 2025-05-15 10:55:21
 */
@RequiredArgsConstructor
@Service
public class PlantVarietyServiceImpl extends ServiceImpl<PlantVarietyMapper, PlantVarietyEntity> implements PlantVarietyService {

    private final PlantVarietyOwnerService ownerService;
    private final PlantVarietyBreederService breederService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createVariety(PlantVarietyCreateRequest request) {
        PlantVarietyEntity entity = BeanUtil.copyProperties(request.getMain(), PlantVarietyEntity.class);
        this.save(entity);
        this.replaceOwners(entity.getId(), request.getOwners());
        this.replaceBreeders(entity.getId(), request.getBreeders());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateVariety(PlantVarietyUpdateRequest request) {
        PlantVarietyEntity entity = BeanUtil.copyProperties(request.getMain(), PlantVarietyEntity.class);
        this.updateById(entity);
        this.replaceOwners(entity.getId(), request.getOwners());
        this.replaceBreeders(entity.getId(), request.getBreeders());
        return Boolean.TRUE;
    }

    @Override
    public IPage<PlantVarietyResponse> pageResult(PlantVarietyPageRequest request) {
        LambdaQueryWrapper<PlantVarietyEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                w.like(PlantVarietyEntity::getRightNo, request.getKeyword())
                        .or().like(PlantVarietyEntity::getName, request.getKeyword()));
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PlantVarietyEntity::getDeptId, request.getDeptId());
        wrapper.ge(StrUtil.isNotBlank(request.getAuthBeginTime()), PlantVarietyEntity::getAuthDate, request.getAuthBeginTime());
        wrapper.le(StrUtil.isNotBlank(request.getAuthEndTime()), PlantVarietyEntity::getAuthDate, request.getAuthEndTime());
        wrapper.orderByDesc(PlantVarietyEntity::getCreateTime);

        Page<PlantVarietyEntity> page = baseMapper.selectPageByScope(new Page<>(request.getCurrent(), request.getSize()), wrapper, DataScope.of());

        return page.convert(entity -> {
            PlantVarietyResponse res = new PlantVarietyResponse();
            res.setMain(BeanUtil.copyProperties(entity, PlantVarietyVO.class));
            res.setOwners(
                    ownerService.lambdaQuery()
                            .eq(PlantVarietyOwnerEntity::getPlantVarietyId, entity.getId())
                            .list()
                            .stream()
                            .map(o -> BeanUtil.copyProperties(o, PlantVarietyOwnerVO.class))
                            .collect(Collectors.toList())
            );

            res.setBreeders(
                    breederService.lambdaQuery()
                            .eq(PlantVarietyBreederEntity::getPlantVarietyId, entity.getId())
                            .list()
                            .stream()
                            .map(b -> BeanUtil.copyProperties(b, PlantVarietyBreederVO.class))
                            .collect(Collectors.toList())
            );
            return res;
        });
    }

    @Override
    public Boolean replaceOwners(Long plantVarietyId, List<PlantVarietyOwnerVO> owners) {
        ownerService.removeByPlantVarietyIds(Collections.singletonList(plantVarietyId));
        if (CollUtil.isNotEmpty(owners)) {
            List<PlantVarietyOwnerEntity> entities = owners.stream()
                    .map(item -> {
                        PlantVarietyOwnerEntity entity = BeanUtil.copyProperties(item, PlantVarietyOwnerEntity.class);
                        entity.setPlantVarietyId(plantVarietyId);
                        return entity;
                    }).collect(Collectors.toList());
            ownerService.saveBatch(entities);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean replaceBreeders(Long plantVarietyId, List<PlantVarietyBreederVO> breeders) {
        breederService.removeByPlantVarietyIds(Collections.singletonList(plantVarietyId));
        if (CollUtil.isNotEmpty(breeders)) {
            List<PlantVarietyBreederEntity> entities = breeders.stream()
                    .map(item -> {
                        PlantVarietyBreederEntity entity = BeanUtil.copyProperties(item, PlantVarietyBreederEntity.class);
                        entity.setPlantVarietyId(plantVarietyId);
                        return entity;
                    }).collect(Collectors.toList());
            breederService.saveBatch(entities);
        }
        return Boolean.TRUE;
    }

    @Override
    public PlantVarietyResponse getDetail(Long id) {
        PlantVarietyEntity entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        PlantVarietyResponse res = new PlantVarietyResponse();
        res.setMain(BeanUtil.copyProperties(entity, PlantVarietyVO.class));
        res.setOwners(
                ownerService.lambdaQuery()
                        .eq(PlantVarietyOwnerEntity::getPlantVarietyId, entity.getId())
                        .list()
                        .stream()
                        .map(o -> BeanUtil.copyProperties(o, PlantVarietyOwnerVO.class))
                        .collect(Collectors.toList())
        );

        res.setBreeders(
                breederService.lambdaQuery()
                        .eq(PlantVarietyBreederEntity::getPlantVarietyId, entity.getId())
                        .list()
                        .stream()
                        .map(b -> BeanUtil.copyProperties(b, PlantVarietyBreederVO.class))
                        .collect(Collectors.toList())
        );
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeVarieties(List<Long> ids) {
        this.removeBatchByIds(ids);
        ownerService.removeByPlantVarietyIds(ids);
        breederService.removeByPlantVarietyIds(ids);
        return Boolean.TRUE;
    }

}