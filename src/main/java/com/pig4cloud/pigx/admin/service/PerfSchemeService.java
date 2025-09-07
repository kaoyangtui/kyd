package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.perf.*;
import com.pig4cloud.pigx.admin.entity.PerfSchemeEntity;

import java.io.IOException;
import java.util.List;

public interface PerfSchemeService extends IService<PerfSchemeEntity> {
    Boolean doSaveOrUpdateWithRules(PerfSchemeSaveRequest request);

    PerfSchemeWithRulesResponse detailWithRules(Long id);

    IPage<PerfSchemeResponse> pageResult(Page page, PerfSchemePageRequest request);

    Boolean toggleStatus(Long id);

    Boolean remove(List<Long> ids);

    PerfSchemeOverviewResponse overview(Long schemeId);

}