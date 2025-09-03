package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoCreateRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PlatformInfoEntity;

public interface PlatformInfoService extends IService<PlatformInfoEntity> {

    PlatformInfoResponse getDetail();

    boolean updateInfo(PlatformInfoUpdateRequest request);

}