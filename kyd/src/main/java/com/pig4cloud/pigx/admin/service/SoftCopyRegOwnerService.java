package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegOwnerEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SoftCopyRegOwnerService extends IService<SoftCopyRegOwnerEntity> {

    @Transactional(rollbackFor = Exception.class)
    Boolean replaceOwners(Long regId, List<SoftCopyRegOwnerEntity> owners);
}