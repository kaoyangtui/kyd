package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.IcLayoutOwnerEntity;

import java.util.List;

public interface IcLayoutOwnerService extends IService<IcLayoutOwnerEntity> {

    Boolean removeByIcLayoutIds(List<Long> ids);
}