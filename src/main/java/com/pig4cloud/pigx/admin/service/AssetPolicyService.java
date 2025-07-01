package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyCreateRequest;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyPageRequest;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyResponse;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyUpdateRequest;
import com.pig4cloud.pigx.admin.entity.AssetPolicyEntity;

import java.util.List;

public interface AssetPolicyService extends IService<AssetPolicyEntity> {
    IPage<AssetPolicyResponse> pageResult(Page page, AssetPolicyPageRequest request);

    AssetPolicyResponse getDetail(Long id);

    Boolean createPolicy(AssetPolicyCreateRequest request);

    Boolean updatePolicy(AssetPolicyUpdateRequest request);

    Boolean removePolicies(List<Long> ids);
}