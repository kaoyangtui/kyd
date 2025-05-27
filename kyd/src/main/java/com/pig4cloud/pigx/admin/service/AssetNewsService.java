package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsCreateRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsResponse;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsUpdateRequest;
import com.pig4cloud.pigx.admin.entity.AssetNewsEntity;

import java.util.List;

public interface AssetNewsService extends IService<AssetNewsEntity> {
    IPage<AssetNewsResponse> pageResult(Page page, AssetNewsPageRequest request);

    AssetNewsResponse getDetail(Long id);

    Boolean create(AssetNewsCreateRequest request);

    Boolean update(AssetNewsUpdateRequest request);

    Boolean remove(List<Long> ids);

    List<AssetNewsResponse> exportList(AssetNewsPageRequest request);
}