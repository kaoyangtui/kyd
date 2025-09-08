package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutCreateRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutPageRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutResponse;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutUpdateRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * 集成电路布图登记 Service
 */
public interface IcLayoutService extends IService<IcLayoutEntity> {

    Boolean create(IcLayoutCreateRequest request);

    Boolean update(IcLayoutUpdateRequest request);

    Boolean remove(List<Long> ids);

    IcLayoutResponse getDetail(Long id);

    IPage<IcLayoutResponse> pageResult(Page<IcLayoutEntity> page, IcLayoutPageRequest request);

    // ===================== 集成电路布图 =====================
    List<PerfEventDTO> fetchIcLayoutEvents(RuleEventEnum evt,
                                           PerfRuleEntity rule,
                                           LocalDate start,
                                           LocalDate end);
}