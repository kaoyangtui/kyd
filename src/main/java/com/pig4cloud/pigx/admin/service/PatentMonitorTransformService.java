package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformResponse;
import com.pig4cloud.pigx.admin.entity.PatentMonitorTransformEntity;

import java.time.LocalDate;
import java.util.List;

public interface PatentMonitorTransformService extends IService<PatentMonitorTransformEntity> {
    IPage<PatentMonitorTransformResponse> pageResult(Page page, PatentMonitorTransformPageRequest request);

    /**
     * 添加监控（支持批量）
     */
    Boolean create(String pid,
                   String code,
                   String name,
                   LocalDate signDate,
                   LocalDate expireDate,
                   Long createUserId);

    /**
     * 取消监控（支持批量）
     */
    Boolean remove(List<Long> idList);
}