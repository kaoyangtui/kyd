package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.expert.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.entity.DemandSignupEntity;
import com.pig4cloud.pigx.admin.entity.ExpertEntity;

import java.util.List;

/**
 * 专家信息管理 Service
 *
 * @author zhaoliang
 */
public interface ExpertService extends IService<ExpertEntity> {

    IPage<ExpertResponse> pageResult(Page page, ExpertPageRequest request);

    ExpertResponse getDetail(Long id);

    Boolean create(ExpertCreateRequest request);

    Boolean update(ExpertUpdateRequest request);

    Boolean remove(List<Long> ids);

    List<ExpertResponse> exportList(ExpertPageRequest request);

    ExportFieldListResponse exportFields();

    /**
     * 批量上下架
     *
     * @param ids         专家id列表
     * @param shelfStatus 上下架状态（0下架 1上架）
     */
    Boolean shelfByIds(List<Long> ids, Integer shelfStatus);
}
