package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.IpAssignService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.*;
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
@RequestMapping("/ipAssign")
@Tag(name = "知识产权赋权管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class IpAssignController {

    private final IpAssignService ipAssignService;

    @PostMapping("/create")
    @Operation(summary = "新增赋权")
    //@PreAuthorize("@pms.hasPermission('ip_assign_add')")
    public R<Boolean> create(@RequestBody IpAssignCreateRequest request) {
        return R.ok(ipAssignService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新赋权")
    //@PreAuthorize("@pms.hasPermission('ip_assign_edit')")
    public R<Boolean> update(@RequestBody IpAssignUpdateRequest request) {
        return R.ok(ipAssignService.update(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查看详情")
    //@PreAuthorize("@pms.hasPermission('ip_assign_view')")
    public R<IpAssignResponse> detail(@RequestBody IdRequest request) {
        return R.ok(ipAssignService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除")
    //@PreAuthorize("@pms.hasPermission('ip_assign_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(ipAssignService.removeByIds(request.getIds()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询赋权记录")
    //@PreAuthorize("@pms.hasPermission('ip_assign_view')")
    public R<IPage<IpAssignResponse>> page(@ParameterObject Page page, @ParameterObject IpAssignPageRequest request) {
        return R.ok(ipAssignService.pageResult(page, request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('ip_assign_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                IpAssignResponse.BIZ_CODE,
                IpAssignResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "知识产权赋权导出", sheets = {@Sheet(sheetName = "赋权列表")})
    @Operation(summary = "导出赋权记录")
    //@PreAuthorize("@pms.hasPermission('ip_assign_export')")
    public List<Map<String, Object>> export(@RequestBody IpAssignExportWrapperRequest request) {
        IPage<IpAssignResponse> pageData = ipAssignService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                IpAssignResponse.class
        );
    }
}