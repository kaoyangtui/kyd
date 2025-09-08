package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyEntity;

import java.time.LocalDate;
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

    List<PerfEventDTO> fetchSoftwareEvents(RuleEventEnum evt,
                                           PerfRuleEntity rule,
                                           LocalDate start,
                                           LocalDate end);
}