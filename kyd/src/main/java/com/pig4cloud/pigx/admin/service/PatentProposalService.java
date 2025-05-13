package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.vo.PatentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.vo.PatentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.vo.PatentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.vo.PatentProposal.PatentProposalUpdateRequest;

import java.util.List;

public interface PatentProposalService extends IService<PatentProposalEntity> {

    IPage<PatentProposalResponse> pageResult(PatentProposalPageRequest request);

    PatentProposalResponse getDetail(Long id);

    Boolean createProposal(PatentProposalCreateRequest request);

    Boolean updateProposal(PatentProposalUpdateRequest request);

    Boolean removeProposals(List<Long> ids);

    List<PatentProposalResponse> exportList(PatentProposalPageRequest request);
}