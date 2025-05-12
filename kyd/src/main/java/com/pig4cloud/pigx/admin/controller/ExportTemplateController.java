package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.entity.ExportTemplateEntity;
import com.pig4cloud.pigx.admin.service.ExportTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 用户导出字段模板配置表
 *
 * @author pigx
 * @date 2025-05-12 09:07:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/exportTemplate")
@Tag(description = "exportTemplate", name = "用户导出字段模板配置表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExportTemplateController {

    private final ExportTemplateService exportTemplateService;


    @PostMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('admin_export_template_view')")
    public R<IPage<ExportTemplateResponse>> page(@RequestBody ExportTemplatePageRequest request) {
        return R.ok(exportTemplateService.pageTemplate(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ExportTemplateResponse> detail(@RequestBody IdRequest request) {
        return R.ok(exportTemplateService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增导出模板")
    public R<Boolean> create(@RequestBody ExportTemplateCreateRequest request) {
        return R.ok(exportTemplateService.createTemplate(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改导出模板")
    public R<Boolean> update(@RequestBody ExportTemplateUpdateRequest request) {
        return R.ok(exportTemplateService.updateTemplate(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除导出模板")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(exportTemplateService.removeTemplates(request.getIds()));
    }

    @PostMapping("/set-default")
    @Operation(summary = "设置为默认")
    @SysLog("设置模板为默认")
    public R<Boolean> setDefault(@RequestBody IdRequest request) {
        return R.ok(exportTemplateService.setDefault(request.getId()));
    }
}