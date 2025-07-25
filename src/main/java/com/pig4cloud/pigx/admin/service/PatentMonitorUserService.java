package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserResponse;
import com.pig4cloud.pigx.admin.entity.PatentMonitorUserEntity;

import java.util.List;

public interface PatentMonitorUserService extends IService<PatentMonitorUserEntity> {

    /**
     * 分页查询（含导出）
     */
    IPage<PatentMonitorUserResponse> pageResult(Page page, PatentMonitorUserPageRequest request);

    /**
     * 添加监控（支持批量）
     */
    Boolean create(List<String> pidList);

    /**
     * 取消监控（支持批量）
     */
    Boolean remove(List<String> pidList);
}