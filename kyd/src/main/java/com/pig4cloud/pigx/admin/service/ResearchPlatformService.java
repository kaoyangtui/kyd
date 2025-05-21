package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.ShelfRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformPageRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformResponse;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformUpdateRequest;
import com.pig4cloud.pigx.admin.entity.ResearchPlatformEntity;

import java.util.List;

public interface ResearchPlatformService extends IService<ResearchPlatformEntity> {

    ResearchPlatformResponse create(ResearchPlatformCreateRequest request);

    Boolean update(ResearchPlatformUpdateRequest request);

    IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request);

    ResearchPlatformResponse getDetail(Long id);

    Boolean updateShelfStatus(ShelfRequest request);

    Boolean remove(IdListRequest request);

}
