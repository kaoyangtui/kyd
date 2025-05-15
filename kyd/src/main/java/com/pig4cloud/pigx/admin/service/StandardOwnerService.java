package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.StandardOwnerEntity;
import com.pig4cloud.pigx.admin.vo.standard.StandardOwnerVO;

import java.util.List;

public interface StandardOwnerService extends IService<StandardOwnerEntity> {

    Boolean removeByStandardIds(List<Long> ids);

    Boolean replaceOwners(Long id, List<StandardOwnerVO> owners);
}