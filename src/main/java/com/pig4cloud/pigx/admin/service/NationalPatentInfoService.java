package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowPageReq;
import com.pig4cloud.pigx.admin.dto.patent.PatentDetailResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchListReq;
import com.pig4cloud.pigx.admin.entity.NationalPatentInfoEntity;

import java.util.List;

public interface NationalPatentInfoService extends IService<NationalPatentInfoEntity> {

    IPage<PatentInfoResponse> searchList(PatentSearchListReq req);
    void create(NationalPatentInfoEntity patentInfo, String message);
    void upsertBatchFromMessages(List<String> messages, int batchSize);
    PatentDetailResponse getDetailByPid(String pid);
    IPage<PatentInfoResponse> pageMyFollow(Long userId, NationalPatentFollowPageReq req);
}