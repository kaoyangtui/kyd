package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.notice.NoticeCreateRequest;
import com.pig4cloud.pigx.admin.dto.notice.NoticePageRequest;
import com.pig4cloud.pigx.admin.dto.notice.NoticeResponse;
import com.pig4cloud.pigx.admin.dto.notice.NoticeUpdateRequest;
import com.pig4cloud.pigx.admin.entity.NoticeEntity;

import java.util.List;

public interface NoticeService extends IService<NoticeEntity> {
    IPage<NoticeResponse> pageResult(Page page, NoticePageRequest request);

    NoticeResponse getDetail(Long id);

    Boolean create(NoticeCreateRequest request);

    Boolean update(NoticeUpdateRequest request);

    Boolean remove(List<Long> ids);

}