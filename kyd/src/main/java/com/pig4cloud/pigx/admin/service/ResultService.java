package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.admin.vo.result.ResultCreateRequest;
import com.pig4cloud.pigx.admin.vo.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.vo.result.ResultResponse;
import com.pig4cloud.pigx.admin.vo.result.ResultShelfRequest;

/**
 * @author zhaoliang
 */
public interface ResultService extends IService<ResultEntity> {

    ResultResponse createResult(ResultCreateRequest request);

    Boolean updateResult(Long id, ResultCreateRequest request);

    IPage<ResultResponse> pageResult(ResultPageRequest request);

    Boolean updateShelfStatus(ResultShelfRequest request);

    ResultResponse getDetail(Long id);

    Boolean removeResult(IdListRequest request);
}