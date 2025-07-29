package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistCreateRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistPageRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistResponse;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistUpdateRequest;
import com.pig4cloud.pigx.admin.entity.BlacklistEntity;

public interface BlacklistService extends IService<BlacklistEntity> {
    BlacklistResponse createBlacklist(BlacklistCreateRequest request);

    Boolean updateBlacklist(BlacklistUpdateRequest request);

    IPage<BlacklistResponse> pageResult(Page page, BlacklistPageRequest request);

    Boolean removeBlacklist(IdListRequest request);
}