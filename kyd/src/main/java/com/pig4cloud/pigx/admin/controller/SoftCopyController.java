package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.service.SoftCopyService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldResponse;
import com.pig4cloud.pigx.admin.vo.softCopy.*;
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

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/softCopy")
@Tag(name = "软著提案管理", description = "软著提案管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SoftCopyController {

    private final SoftCopyService softCopyService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('soft_copy_view')")
    public R<IPage<SoftCopyResponse>> page(@RequestBody SoftCopyPageRequest request) {
        return R.ok(softCopyService.pageResult(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('soft_copy_view')")
    public R<SoftCopyResponse> detail(@RequestBody IdRequest request) {
        return R.ok(softCopyService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增软著提案")
    @PreAuthorize("@pms.hasPermission('soft_copy_add')")
    public R<Boolean> create(@RequestBody SoftCopyCreateRequest request) {
        return R.ok(softCopyService.createProposal(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改软著提案")
    @PreAuthorize("@pms.hasPermission('soft_copy_edit')")
    public R<Boolean> update(@RequestBody SoftCopyUpdateRequest request) {
        return R.ok(softCopyService.updateProposal(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除软著提案")
    @PreAuthorize("@pms.hasPermission('soft_copy_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(softCopyService.removeProposals(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    @PreAuthorize("@pms.hasPermission('soft_copy_export')")
    public R<ExportFieldListResponse> exportFields() {
        List<ExportFieldResponse> fields = ExportFieldHelper.getFieldsFromDto(SoftCopyResponse.class);
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(SoftCopyResponse.BIZ_CODE);
        response.setFields(fields);
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "软著提案导出", sheets = {@Sheet(sheetName = "软著提案列表")})
    @Operation(summary = "导出")
    @PreAuthorize("@pms.hasPermission('soft_copy_export')")
    public List<Map<String, Object>> export(@RequestBody SoftCopyExportWrapperRequest request) {
        IPage<SoftCopyResponse> pageData = softCopyService.pageResult(request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys());
    }
}
