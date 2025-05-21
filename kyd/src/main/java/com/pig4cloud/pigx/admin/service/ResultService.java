package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.dto.*;
import com.pig4cloud.pigx.admin.dto.result.*;

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