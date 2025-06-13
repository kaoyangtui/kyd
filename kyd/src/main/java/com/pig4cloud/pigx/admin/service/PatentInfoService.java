package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;

/**
 * @author zhaoliang
 */
public interface PatentInfoService extends IService<PatentInfoEntity> {

    IPage<PatentInfoResponse> pageResult(Page page, PatentPageRequest request);

    IPage<PatentSearchResponse> searchPatent(Page page, PatentSearchRequest request);

    IPage<PatentSearchListRes> searchList(PatentSearchListReq req);

    void create(PatentInfoEntity patentInfo, String message);
}