package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.StandardService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.standard.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/standard")
@Tag(name = "标准信息管理", description = "标准信息管理")
public class StandardController {

    private final StandardService standardService;

    @GetMapping("/page")
    @Operation(summary = "分页查询标准信息")
    @PreAuthorize("@pms.hasPermission('standard_view')")
    public R<IPage<StandardResponse>> page(@ParameterObject Page page, @ParameterObject StandardPageRequest request) {
        return R.ok(standardService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询标准信息详情")
    @PreAuthorize("@pms.hasPermission('standard_view')")
    public R<StandardResponse> detail(@RequestBody Long id) {
        return R.ok(standardService.getDetail(id));
    }

    @PostMapping("/create")
    @Operation(summary = "新增标准信息")
    @PreAuthorize("@pms.hasPermission('standard_add')")
    public R<Boolean> create(@RequestBody StandardCreateRequest request) {
        return R.ok(standardService.createStandard(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改标准信息")
    @PreAuthorize("@pms.hasPermission('standard_edit')")
    public R<Boolean> update(@RequestBody StandardUpdateRequest request) {
        return R.ok(standardService.updateStandard(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除标准信息")
    @PreAuthorize("@pms.hasPermission('standard_del')")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.ok(standardService.removeStandards(ids));
    }

    @PostMapping("/export")
    @ResponseExcel(name = "标准信息导出", sheets = {@Sheet(sheetName = "标准信息列表")})
    @Operation(summary = "导出标准信息")
    @PreAuthorize("@pms.hasPermission('standard_export')")
    public List<Map<String, Object>> export(@RequestBody StandardExportWrapperRequest request) {
        IPage<StandardResponse> pageData = standardService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), StandardResponse.class);
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取标准导出字段列表")
    @PreAuthorize("@pms.hasPermission('standard_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = ExportFieldHelper.buildExportFieldList(
                StandardResponse.BIZ_CODE,
                StandardMainVO.class
        );
        return R.ok(response);
    }


}
