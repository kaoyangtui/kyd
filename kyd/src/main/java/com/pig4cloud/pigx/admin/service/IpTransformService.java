package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.vo.ipTransform.*;

import java.util.List;

public interface IpTransformService extends IService<IpTransformEntity> {

    Boolean create(IpTransformCreateRequest request);

    Boolean update(IpTransformUpdateRequest request);

    IpTransformResponse getDetail(Long id);

    Boolean removeByIds(List<Long> ids);

    IPage<IpTransformResponse> pageResult(Page page, IpTransformPageRequest request);
}