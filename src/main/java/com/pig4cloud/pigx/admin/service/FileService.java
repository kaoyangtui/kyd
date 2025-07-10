package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.FileGroupTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.file.FilePageRequest;
import com.pig4cloud.pigx.admin.dto.file.FileResponse;
import com.pig4cloud.pigx.admin.dto.file.FileUpdateRequest;
import com.pig4cloud.pigx.admin.entity.FileEntity;

import java.util.List;

public interface FileService extends IService<FileEntity> {

    Boolean create(FileCreateRequest request);

    Boolean batchCreate(List<FileCreateRequest> requestList);

    Boolean update(FileUpdateRequest request);

    Boolean removeByIds(List<Long> ids);

    IPage<FileResponse> pageResult(Page page, FilePageRequest request);

    FileCreateRequest getFileCreateRequest(String fileName,
                                           String code,
                                           String applyType,
                                           String subjectName,
                                           String bizType);
    String uploadFileByUrl(String url, String dir, FileGroupTypeEnum type);

}