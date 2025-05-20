package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.DemandInService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.vo.IdListRequest;
import com.pig4cloud.pigx.admin.vo.IdRequest;
import com.pig4cloud.pigx.admin.vo.demandIn.*;
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
@RequestMapping("/demandIn")
@Tag(name = "校内需求管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DemandInController {

    private final DemandInService demandInService;

    @PostMapping("/create")
    @Operation(summary = "新增校内需求")
    @PreAuthorize("@pms.hasPermission('demand_in_add')")
    public R<Boolean> create(@RequestBody DemandInCreateRequest request) {
        return R.ok(demandInService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改校内需求")
    @PreAuthorize("@pms.hasPermission('demand_in_edit')")
    public R<Boolean> update(@RequestBody DemandInUpdateRequest request) {
        return R.ok(demandInService.update(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查看详情")
    @PreAuthorize("@pms.hasPermission('demand_in_view')")
    public R<DemandInResponse> detail(@RequestBody IdRequest request) {
        return R.ok(demandInService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除")
    @PreAuthorize("@pms.hasPermission('demand_in_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(demandInService.removeByIds(request.getIds()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('demand_in_view')")
    public R<IPage<DemandInResponse>> page(@ParameterObject Page page, @ParameterObject DemandInPageRequest request) {
        return R.ok(demandInService.pageResult(page, request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    @PreAuthorize("@pms.hasPermission('demand_in_export')")
    public R<ExportFieldListResponse> exportFields() {
        return R.ok(ExportFieldHelper.buildExportFieldList(
                DemandInResponse.BIZ_CODE,
                DemandInResponse.class
        ));
    }

    @PostMapping("/export")
    @ResponseExcel(name = "校内需求导出", sheets = {@Sheet(sheetName = "需求列表")})
    @Operation(summary = "导出需求记录")
    @PreAuthorize("@pms.hasPermission('demand_in_export')")
    public List<Map<String, Object>> export(@RequestBody DemandInExportWrapperRequest request) {
        IPage<DemandInResponse> pageData = demandInService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                DemandInResponse.class
        );
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架")
    @PreAuthorize("@pms.hasPermission('demand_in_edit')")
    public R<Boolean> updateShelfStatus(@RequestBody ShelfStatusRequest request) {
        return R.ok(demandInService.updateShelfStatus(request.getId(), request.getShelfStatus()));
    }

}