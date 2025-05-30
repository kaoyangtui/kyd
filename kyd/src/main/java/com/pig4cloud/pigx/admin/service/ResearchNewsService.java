package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsUpdateRequest;
import com.pig4cloud.pigx.admin.entity.ResearchNewsEntity;

import java.util.List;

public interface ResearchNewsService extends IService<ResearchNewsEntity> {
    /**
     * 分页查询
     */
    IPage<ResearchNewsResponse> pageResult(Page page, ResearchNewsPageRequest request);

    /**
     * 详情
     */
    ResearchNewsResponse getDetail(Long id);

    /**
     * 新增
     */
    Boolean createNews(ResearchNewsCreateRequest request);

    /**
     * 修改
     */
    Boolean updateNews(ResearchNewsUpdateRequest request);

    /**
     * 删除
     */
    Boolean removeNews(List<Long> ids);

    /**
     * 获取导出字段
     */
    ExportFieldListResponse exportFields();
}