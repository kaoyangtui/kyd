package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhaoliang
 */
public interface PatentInfoService extends IService<PatentInfoEntity> {

    IPage<PatentInfoResponse> pageResult(Page page, PatentPageRequest request);

    IPage<PatentSearchResponse> searchPatent(Page page, PatentSearchRequest request);

    IPage<PatentSearchListRes> searchList(PatentSearchListReq req);

    void create(PatentInfoEntity patentInfo, String message);

    PatentDetailResponse getDetailByPid(String pid);

    PatentDetailResponse getDetailImgByPid(String pid);

    String getDetailPdfByPid(String pid);

    List<PatentTypeSummaryVO> patentTypeSummary(PatentPageRequest request);

    List<PerfEventDTO> fetchPatentApplyPub(IpTypeEnum type,
                                           PerfRuleEntity rule,
                                           LocalDate start,
                                           LocalDate end);

    List<PerfEventDTO> fetchPatentGrantPub(IpTypeEnum type,
                                           PerfRuleEntity rule,
                                           LocalDate start,
                                           LocalDate end);
}