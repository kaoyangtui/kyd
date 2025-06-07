package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.result.*;
import com.pig4cloud.pigx.admin.entity.ResultEntity;

/**
 * @author zhaoliang
 */
public interface ResultService extends IService<ResultEntity> {

    ResultResponse createResult(ResultCreateRequest request);

    Boolean updateResult(ResultUpdateRequest request);

    IPage<ResultResponse> pageResult(Page page, ResultPageRequest request);

    Boolean updateShelfStatus(ResultShelfRequest request);

    ResultResponse getDetail(Long id);

    Boolean removeResult(IdListRequest request);
}