package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;

import java.util.List;

public interface DemandReceiveService extends IService<DemandReceiveEntity> {

    Boolean receive(DemandReceiveRequest request);

    IPage<DemandReceiveResponse> pageResult(Page page, DemandReceivePageRequest request);

    void receive(DemandEntity entity, List<Long> userIds);

    int markReadBatch(List<Long> ids);
}