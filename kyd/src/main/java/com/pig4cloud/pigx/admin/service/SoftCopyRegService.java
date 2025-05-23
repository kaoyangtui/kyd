package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegUpdateRequest;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegEntity;

import java.util.List;

public interface SoftCopyRegService extends IService<SoftCopyRegEntity> {

    /**
     * 新增软著登记
     */
    Boolean create(SoftCopyRegCreateRequest request);

    /**
     * 修改软著登记
     */
    Boolean update(SoftCopyRegUpdateRequest request);

    /**
     * 分页查询软著登记
     */
    IPage<SoftCopyRegResponse> pageResult(Page page, SoftCopyRegPageRequest request);

    /**
     * 查询详情
     */
    SoftCopyRegResponse getDetail(Long id);

    /**
     * 批量删除
     */
    Boolean remove(List<Long> ids);
}
