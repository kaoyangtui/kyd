package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.demand.DemandSignupRequest;
import com.pig4cloud.pigx.admin.entity.DemandSignupEntity;

public interface DemandSignupService extends IService<DemandSignupEntity> {
    Boolean signup(DemandSignupRequest request);
}
