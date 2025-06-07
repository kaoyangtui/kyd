package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;

import java.util.List;

public interface PatentProposalService extends IService<PatentProposalEntity> {

    IPage<PatentProposalResponse> pageResult(Page page, PatentProposalPageRequest request);

    PatentProposalResponse getDetail(Long id);

    Boolean createProposal(PatentProposalCreateRequest request);

    Boolean updateProposal(PatentProposalUpdateRequest request);

    Boolean removeProposals(List<Long> ids);

}