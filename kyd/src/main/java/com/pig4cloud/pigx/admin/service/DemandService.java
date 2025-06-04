package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.dto.demand.*;

import java.util.List;

public interface DemandService extends IService<DemandEntity> {

    Boolean create(DemandCreateRequest request);

    Boolean update(DemandUpdateRequest request);

    DemandResponse getDetail(Long id);

    Boolean removeByIds(List<Long> ids);

    IPage<DemandResponse> pageResult(Page page, DemandPageRequest request);

    Boolean updateShelfStatus(DemandShelfRequest request);
}