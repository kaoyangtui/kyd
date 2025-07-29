package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;

public interface DemandReceiveService extends IService<DemandReceiveEntity> {

    Boolean receive(DemandReceiveRequest request);

    IPage<DemandReceiveResponse> pageResult(Page page, DemandReceivePageRequest request);
}