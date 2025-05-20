package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PlantVarietyEntity;
import com.pig4cloud.pigx.admin.vo.plantVariety.*;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface PlantVarietyService extends IService<PlantVarietyEntity> {
    IPage<PlantVarietyResponse> pageResult(Page page, PlantVarietyPageRequest request);

    PlantVarietyResponse getDetail(Long id);

    Boolean createVariety(PlantVarietyCreateRequest request);

    Boolean updateVariety(PlantVarietyUpdateRequest request);

    Boolean removeVarieties(List<Long> ids);
    
    Boolean replaceOwners(Long plantVarietyId, List<PlantVarietyOwnerVO> owners);

    Boolean replaceBreeders(Long plantVarietyId, List<PlantVarietyBreederVO> breeders);
}