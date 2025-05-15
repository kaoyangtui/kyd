package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.IcLayoutCreatorInEntity;

import java.util.List;

public interface IcLayoutCreatorInService extends IService<IcLayoutCreatorInEntity> {

    Boolean removeByIcLayoutIds(List<Long> ids);
}