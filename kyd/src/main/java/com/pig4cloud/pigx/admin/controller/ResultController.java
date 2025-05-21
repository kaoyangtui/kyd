package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.dto.result.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.service.ResultService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-11 15:44:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/result")
@Tag(description = "科研成果表管理", name = "科研成果表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/create")
    @Operation(summary = "新增科研成果")
    @PreAuthorize("@pms.hasPermission('result_add')")
    public R<ResultResponse> create(@RequestBody ResultCreateRequest request) {
        return R.ok(resultService.createResult(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新科研成果")
    @PreAuthorize("@pms.hasPermission('result_edit')")
    public R<Boolean> update(@RequestBody ResultUpdateRequest request) {
        return R.ok(resultService.updateResult(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询科研成果")
    @PreAuthorize("@pms.hasPermission('result_view')")
    public R<IPage<ResultResponse>> page(@ParameterObject Page page, @ParameterObject ResultPageRequest request) {
        return R.ok(resultService.pageResult(page, request));
    }

    @PostMapping("/shelf")
    @Operation(summary = "成果上下架")
    @PreAuthorize("@pms.hasPermission('result_edit')")
    public R<Boolean> shelf(@RequestBody ResultShelfRequest request) {
        return R.ok(resultService.updateShelfStatus(request));
    }


    @PostMapping("/detail")
    @Operation(summary = "查询成果详情")
    @PreAuthorize("@pms.hasPermission('result_view')")
    public R<ResultResponse> detail(@RequestBody IdRequest request) {
        return R.ok(resultService.getDetail(request.getId()));
    }


    @PostMapping("/remove")
    @Operation(summary = "删除科研成果")
    @SysLog("删除科研成果")
    @PreAuthorize("@pms.hasPermission('result_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(resultService.removeResult(request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResultResponse.BIZ_CODE,
                ResultResponse.class
        );
        return R.ok(fields);
    }


    @PostMapping("/export")
    @ResponseExcel(name = "科研成果导出", sheets = {@Sheet(sheetName = "科研成果列表")})
    @Operation(summary = "导出成果")
    @PreAuthorize("@pms.hasPermission('result_export')")
    public List<Map<String, Object>> export(@RequestBody ResultExportWrapperRequest request) {
        IPage<ResultResponse> pageData = resultService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), ResultResponse.class);
    }


}