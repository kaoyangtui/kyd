package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.plantVariety.ResearchPlatformExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.*;
import com.pig4cloud.pigx.admin.service.ResearchPlatformService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchPlatform")
@Tag(name = "科研平台管理", description = "科研平台管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchPlatformController {

    private final ResearchPlatformService researchPlatformService;

    @PostMapping("/create")
    @Operation(summary = "新增科研平台")
    @PreAuthorize("@pms.hasPermission('researchPlatform_add')")
    public R<ResearchPlatformResponse> create(@RequestBody ResearchPlatformCreateRequest request) {
        return R.ok(researchPlatformService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新科研平台")
    @PreAuthorize("@pms.hasPermission('researchPlatform_edit')")
    public R<Boolean> update(@RequestBody ResearchPlatformUpdateRequest request) {
        return R.ok(researchPlatformService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询科研平台")
    @PreAuthorize("@pms.hasPermission('researchPlatform_view')")
    public R<IPage<ResearchPlatformResponse>> page(@ParameterObject Page page, @ParameterObject ResearchPlatformPageRequest request) {
        return R.ok(researchPlatformService.pageResult(page, request));
    }

    @PostMapping("/shelf")
    @Operation(summary = "科研平台上下架")
    @PreAuthorize("@pms.hasPermission('researchPlatform_edit')")
    public R<Boolean> shelf(@RequestBody ShelfRequest request) {
        return R.ok(researchPlatformService.updateShelfStatus(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "科研平台详情")
    @PreAuthorize("@pms.hasPermission('researchPlatform_view')")
    public R<ResearchPlatformResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchPlatformService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除科研平台")
    @SysLog("删除科研平台")
    @PreAuthorize("@pms.hasPermission('researchPlatform_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchPlatformService.remove(request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        return R.ok(ExportFieldHelper.buildExportFieldList(
                ResearchPlatformResponse.BIZ_CODE,
                ResearchPlatformResponse.class
        ));
    }

    @PostMapping("/export")
    @ResponseExcel(name = "科研平台导出", sheets = {@Sheet(sheetName = "科研平台列表")})
    @Operation(summary = "导出科研平台")
    @PreAuthorize("@pms.hasPermission('researchPlatform_export')")
    public List<Map<String, Object>> export(@RequestBody ResearchPlatformExportWrapperRequest request) {
        IPage<ResearchPlatformResponse> pageData = researchPlatformService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), ResearchPlatformResponse.class);
    }
}
