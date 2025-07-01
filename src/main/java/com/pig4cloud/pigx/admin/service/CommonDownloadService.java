package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadCreateRequest;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadPageRequest;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadResponse;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CommonDownloadEntity;

import java.util.List;

public interface CommonDownloadService extends IService<CommonDownloadEntity> {

    IPage<CommonDownloadResponse> pageResult(Page page, CommonDownloadPageRequest request);

    CommonDownloadResponse getDetail(Long id);

    Boolean create(CommonDownloadCreateRequest request);

    Boolean update(CommonDownloadUpdateRequest request);

    Boolean remove(List<Long> ids);
}