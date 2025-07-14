package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimCreateRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimPageRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentUnClaimRequest;
import com.pig4cloud.pigx.admin.entity.PatentClaimEntity;

import java.util.List;

public interface PatentClaimService extends IService<PatentClaimEntity> {
    IPage<PatentClaimResponse> pageResult(Page page, PatentClaimPageRequest request);

    PatentClaimResponse getDetail(Long id);

    Boolean removeClaims(List<Long> ids);

    Boolean claim(PatentClaimCreateRequest req);

    Boolean unClaim(PatentUnClaimRequest req);

}