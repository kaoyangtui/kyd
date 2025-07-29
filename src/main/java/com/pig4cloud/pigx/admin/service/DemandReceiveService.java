package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;

public interface DemandReceiveService extends IService<DemandReceiveEntity> {

    Boolean receive(DemandReceiveRequest request);
}