package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.service.SoftCopyRegService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.admin.vo.ExportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.vo.ExportExecute.ExportFieldResponse;
import com.pig4cloud.pigx.admin.vo.SoftCopyReg.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/softCopyReg")
@Tag(name = "软著登记管理", description = "软著登记管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SoftCopyRegController {

    private final SoftCopyRegService softCopyRegService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_view')")
    public R<IPage<SoftCopyRegResponse>> page(@RequestBody SoftCopyRegPageRequest request) {
        return R.ok(softCopyRegService.pageResult(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_view')")
    public R<SoftCopyRegResponse> detail(@RequestBody IdRequest request) {
        return R.ok(softCopyRegService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增软著登记")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_add')")
    public R<Boolean> create(@RequestBody SoftCopyRegCreateRequest request) {
        return R.ok(softCopyRegService.createReg(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改软著登记")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_edit')")
    public R<Boolean> update(@RequestBody SoftCopyRegUpdateRequest request) {
        return R.ok(softCopyRegService.updateReg(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除软著登记")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(softCopyRegService.removeRegs(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_export')")
    public R<ExportFieldListResponse> exportFields() {
        List<ExportFieldResponse> fields = ExportFieldHelper.getFieldsFromDto(SoftCopyRegResponse.class);
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(SoftCopyRegResponse.BIZ_CODE);
        response.setFields(fields);
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "软著登记导出", sheets = {@Sheet(sheetName = "软著登记列表")})
    @Operation(summary = "导出")
    @PreAuthorize("@pms.hasPermission('soft_copy_reg_export')")
    public List<Map<String, Object>> export(@RequestBody SoftCopyRegExportWrapperRequest request) {
        IPage<SoftCopyRegResponse> pageData = softCopyRegService.pageResult(request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys());
    }

}
