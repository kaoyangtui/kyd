package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface OwnerService extends IService<OwnerEntity> {

    void replaceOwners(String code, List<OwnerEntity> owners);
}