package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.researchPlatform.*;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface ResearchPlatformService {

    /**
     * 分页查询科研平台
     */
    IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request);

    /**
     * 获取平台详情
     */
    ResearchPlatformResponse getDetail(Long id);

    /**
     * 创建科研平台
     */
    Boolean createPlatform(ResearchPlatformCreateRequest request);

    /**
     * 更新科研平台
     */
    Boolean updatePlatform(ResearchPlatformUpdateRequest request);

    /**
     * 删除科研平台
     */
    Boolean removePlatforms(List<Long> ids);

    /**
     * 变更上下架状态
     */
    Boolean changeShelfStatus(ResearchPlatformShelfRequest request);

}
