package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.DemandService;
import com.pig4cloud.pigx.admin.service.DemandSignupService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.vo.IdListRequest;
import com.pig4cloud.pigx.admin.vo.IdRequest;
import com.pig4cloud.pigx.admin.vo.demand.*;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
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
@RequestMapping("/demand")
@Tag(name = "企业需求管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DemandController {

    private final DemandService demandService;
    private final DemandSignupService demandSignupService;

    @PostMapping("/create")
    @Operation(summary = "新增企业需求")
    @PreAuthorize("@pms.hasPermission('demand_add')")
    public R<Boolean> create(@RequestBody DemandCreateRequest request) {
        return R.ok(demandService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新企业需求")
    @PreAuthorize("@pms.hasPermission('demand_edit')")
    public R<Boolean> update(@RequestBody DemandUpdateRequest request) {
        return R.ok(demandService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询企业需求")
    @PreAuthorize("@pms.hasPermission('demand_view')")
    public R<IPage<DemandResponse>> page(@ParameterObject Page page, @ParameterObject DemandPageRequest request) {
        return R.ok(demandService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "企业需求详情")
    @PreAuthorize("@pms.hasPermission('demand_view')")
    public R<DemandResponse> detail(@RequestBody IdRequest request) {
        return R.ok(demandService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除企业需求")
    @PreAuthorize("@pms.hasPermission('demand_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(demandService.removeByIds(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(DemandResponse.BIZ_CODE);
        response.setFields(ExportFieldHelper.getFieldsFromDto(DemandResponse.class));
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "企业需求导出", sheets = {@Sheet(sheetName = "需求列表")})
    @Operation(summary = "导出企业需求")
    @PreAuthorize("@pms.hasPermission('demand_export')")
    public List<Map<String, Object>> export(@RequestBody DemandExportWrapperRequest request) {
        IPage<DemandResponse> pageData = demandService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), DemandResponse.class);
    }

    @PostMapping("/signup")
    @Operation(summary = "提交企业需求报名")
    public R<Boolean> signup(@RequestBody DemandSignupRequest request) {
        return R.ok(demandSignupService.signup(request));
    }

}