package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchListReq;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchListRes;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchResponse;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;

/**
 * @author zhaoliang
 */
public interface PatentInfoService extends IService<PatentInfoEntity> {

    IPage<PatentSearchResponse> searchPatent(Page page, PatentSearchRequest request);

    IPage<PatentSearchListRes> searchList(PatentSearchListReq req);

    void create(PatentInfoEntity patentInfo, String message);
}