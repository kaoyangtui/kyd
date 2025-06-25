package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoCreateRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoPageRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PlatformInfoEntity;

public interface PlatformInfoService extends IService<PlatformInfoEntity> {
    IPage<PlatformInfoResponse> pageResult(Page page, PlatformInfoPageRequest request);

    PlatformInfoResponse getDetail(Long id);

    boolean createInfo(PlatformInfoCreateRequest request);

    boolean updateInfo(PlatformInfoUpdateRequest request);

}