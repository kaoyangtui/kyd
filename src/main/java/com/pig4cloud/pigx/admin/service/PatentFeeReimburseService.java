package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.patentFee.PatentFeeReimburseCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentFee.PatentFeeReimbursePageRequest;
import com.pig4cloud.pigx.admin.dto.patentFee.PatentFeeReimburseResponse;
import com.pig4cloud.pigx.admin.dto.patentFee.PatentFeeReimburseUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PatentFeeReimburseEntity;

public interface PatentFeeReimburseService extends IService<PatentFeeReimburseEntity> {

    PatentFeeReimburseResponse createPatentFeeReimburse(PatentFeeReimburseCreateRequest request);

    Boolean updatePatentFeeReimburse(PatentFeeReimburseUpdateRequest request);

    IPage<PatentFeeReimburseResponse> pageResult(Page page, PatentFeeReimbursePageRequest request);

    Boolean removePatentFeeReimburse(IdListRequest request);

    PatentFeeReimburseResponse getDetail(Long id);
}