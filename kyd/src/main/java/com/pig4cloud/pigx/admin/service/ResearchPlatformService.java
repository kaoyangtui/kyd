package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformPageRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformResponse;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformUpdateRequest;
import com.pig4cloud.pigx.admin.entity.ResearchPlatformEntity;

import java.util.List;

public interface ResearchPlatformService extends IService<ResearchPlatformEntity> {

    Boolean create(ResearchPlatformCreateRequest request);

    Boolean update(ResearchPlatformUpdateRequest request);

    Boolean remove(List<Long> ids);

    ResearchPlatformResponse getDetail(Long id);

    Boolean shelfByIds(List<Long> ids, Integer shelfStatus);

    IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request);
}
