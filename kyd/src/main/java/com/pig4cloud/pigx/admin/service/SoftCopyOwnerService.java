package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyOwnerEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SoftCopyOwnerService extends IService<SoftCopyOwnerEntity> {

    @Transactional(rollbackFor = Exception.class)
    void replaceOwners(Long softCopyId, List<SoftCopyOwnerEntity> entities);
}