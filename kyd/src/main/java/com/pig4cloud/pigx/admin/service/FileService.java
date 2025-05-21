package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.FileEntity;
import com.pig4cloud.pigx.admin.vo.file.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FileService extends IService<FileEntity> {

    Boolean create(FileCreateRequest request);

    Boolean batchCreate(List<FileCreateRequest> requestList);

    Boolean update(FileUpdateRequest request);

    Boolean removeByIds(List<Long> ids);

    IPage<FileResponse> pageResult(Page page, FilePageRequest request);
}