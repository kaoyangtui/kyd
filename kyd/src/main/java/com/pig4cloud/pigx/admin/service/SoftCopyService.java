package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyEntity;
import com.pig4cloud.pigx.admin.vo.softCopy.SoftCopyCreateRequest;
import com.pig4cloud.pigx.admin.vo.softCopy.SoftCopyPageRequest;
import com.pig4cloud.pigx.admin.vo.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.vo.softCopy.SoftCopyUpdateRequest;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface SoftCopyService extends IService<SoftCopyEntity> {

    /**
     * 分页查询
     */
    IPage<SoftCopyResponse> pageResult(Page page, SoftCopyPageRequest request);

    /**
     * 新增软著提案
     */
    Boolean createProposal(SoftCopyCreateRequest request);

    /**
     * 更新软著提案
     */
    Boolean updateProposal(SoftCopyUpdateRequest request);

    /**
     * 删除软著提案
     */
    Boolean removeProposals(List<Long> ids);

    /**
     * 获取详情
     */
    SoftCopyResponse getDetail(Long id);

}