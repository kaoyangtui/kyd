package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.vo.icLayout.*;

import java.util.List;

public interface IcLayoutService extends IService<IcLayoutEntity> {
    IPage<IcLayoutResponse> pageResult(IcLayoutPageRequest request);

    IcLayoutResponse getDetail(Long id);

    Boolean createLayout(IcLayoutCreateRequest request);

    Boolean updateLayout(IcLayoutUpdateRequest request);

    Boolean removeLayouts(List<Long> ids);

    Boolean replaceOwners(Long icLayoutId, List<IcLayoutOwnerVO> owners);

    Boolean replaceCreators(Long icLayoutId, List<IcLayoutCreatorInVO> creators);

}