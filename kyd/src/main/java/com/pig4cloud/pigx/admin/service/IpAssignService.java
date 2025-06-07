package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignPageRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignUpdateRequest;
import com.pig4cloud.pigx.admin.entity.IpAssignEntity;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface IpAssignService extends IService<IpAssignEntity> {

    Boolean create(IpAssignCreateRequest request);

    Boolean update(IpAssignUpdateRequest request);

    IpAssignResponse getDetail(Long id);

    Boolean removeByIds(List<Long> ids);

    IPage<IpAssignResponse> pageResult(Page page, IpAssignPageRequest request);

}
