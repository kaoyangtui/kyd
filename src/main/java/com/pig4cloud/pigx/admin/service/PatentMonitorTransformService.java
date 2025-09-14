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
     * 取消监控（支持批量）
     */
    Boolean remove(List<Long> idList);
}