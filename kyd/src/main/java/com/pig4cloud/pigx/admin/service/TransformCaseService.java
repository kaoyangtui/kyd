package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseCreateRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCasePageRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseUpdateRequest;
import com.pig4cloud.pigx.admin.entity.TransformCaseEntity;

import java.util.List;

public interface TransformCaseService extends IService<TransformCaseEntity> {
    IPage<TransformCaseResponse> pageResult(Page page, TransformCasePageRequest request);

    TransformCaseResponse getDetail(Long id);

    boolean createCase(TransformCaseCreateRequest request);

    boolean updateCase(TransformCaseUpdateRequest request);

    boolean removeCases(List<Long> ids);

}