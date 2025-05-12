package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.ExportTemplateEntity;
import com.pig4cloud.pigx.admin.vo.ExportTemplateCreateRequest;
import com.pig4cloud.pigx.admin.vo.ExportTemplatePageRequest;
import com.pig4cloud.pigx.admin.vo.ExportTemplateResponse;
import com.pig4cloud.pigx.admin.vo.ExportTemplateUpdateRequest;

import java.util.List;

public interface ExportTemplateService extends IService<ExportTemplateEntity> {
    IPage<ExportTemplateResponse> pageTemplate(ExportTemplatePageRequest request);
    ExportTemplateResponse getDetail(Long id);
    Boolean createTemplate(ExportTemplateCreateRequest request);
    Boolean updateTemplate(ExportTemplateUpdateRequest request);
    Boolean removeTemplates(List<Long> ids);
    Boolean setDefault(Long id);
}