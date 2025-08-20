package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInCreateRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInPageRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInUpdateRequest;
import com.pig4cloud.pigx.admin.entity.DemandInEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DemandInService extends IService<DemandInEntity> {
    Boolean create(DemandInCreateRequest request);

    Boolean update(DemandInUpdateRequest request);

    DemandInResponse getDetail(Long id);

    Boolean removeByIds(List<Long> ids);

    IPage<DemandInResponse> pageResult(Page page, DemandInPageRequest request);

    IPage<DemandInResponse> pageResult(Page page, DemandInPageRequest request, boolean isByScope);

    @Transactional
    Boolean updateShelfStatus(Long id, Integer shelfStatus);
}