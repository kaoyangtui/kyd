package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.IcLayoutService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.*;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
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
 * 集成电路布图管理
 *
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/icLayout")
@Tag(name = "集成电路布图管理")
public class IcLayoutController {

    private final IcLayoutService icLayoutService;

    @GetMapping("/page")
    @Operation(summary = "分页查询集成电路布图")
    @PreAuthorize("@pms.hasPermission('ic_layout_view')")
    public R<IPage<IcLayoutResponse>> page(@ParameterObject Page page, @ParameterObject IcLayoutPageRequest request) {
        return R.ok(icLayoutService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询详情")
    @PreAuthorize("@pms.hasPermission('ic_layout_view')")
    public R<IcLayoutResponse> detail(@RequestBody IdRequest request) {
        return R.ok(icLayoutService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @PreAuthorize("@pms.hasPermission('ic_layout_add')")
    public R<Boolean> create(@RequestBody IcLayoutCreateRequest request) {
        return R.ok(icLayoutService.createLayout(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('ic_layout_edit')")
    public R<Boolean> update(@RequestBody IcLayoutUpdateRequest request) {
        return R.ok(icLayoutService.updateLayout(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('ic_layout_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(icLayoutService.removeLayouts(request.getIds()));
    }

    @PostMapping("/export")
    @ResponseExcel(name = "集成电路布图导出", sheets = {@Sheet(sheetName = "布图列表")})
    @Operation(summary = "导出集成电路布图")
    @PreAuthorize("@pms.hasPermission('ic_layout_export')")
    public List<Map<String, Object>> export(@RequestBody ExportWrapperRequest<IcLayoutPageRequest> request) {
        IPage<IcLayoutResponse> pageData = icLayoutService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), IcLayoutResponse.class);
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    @PreAuthorize("@pms.hasPermission('ic_layout_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = ExportFieldHelper.buildExportFieldList(
                IcLayoutResponse.BIZ_CODE,
                IcLayoutMainVO.class
        );
        return R.ok(response);
    }

}
