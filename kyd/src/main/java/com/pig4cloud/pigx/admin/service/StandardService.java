package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.standard.StandardCreateRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardPageRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardResponse;
import com.pig4cloud.pigx.admin.dto.standard.StandardUpdateRequest;
import com.pig4cloud.pigx.admin.entity.StandardEntity;

import java.util.List;

public interface StandardService extends IService<StandardEntity> {

    IPage<StandardResponse> pageResult(Page page, StandardPageRequest request);

    StandardResponse getDetail(Long id);

    Boolean create(StandardCreateRequest request);

    Boolean update(StandardUpdateRequest request);

    Boolean removeByIds(List<Long> ids);
}