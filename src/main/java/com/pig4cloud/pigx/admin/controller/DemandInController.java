package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.DemandInService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demandIn")
@Tag(name = "校内需求管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DemandInController {

    private final DemandInService demandInService;

    @PostMapping("/create")
    @Operation(summary = "新增校内需求")
    //@PreAuthorize("@pms.hasPermission('demand_in_add')")
    public R<Boolean> create(@RequestBody @Valid DemandInCreateRequest request) {
        return R.ok(demandInService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改校内需求")
    //@PreAuthorize("@pms.hasPermission('demand_in_edit')")
    public R<Boolean> update(@RequestBody DemandInUpdateRequest request) {
        return R.ok(demandInService.update(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查看详情")
    //@PreAuthorize("@pms.hasPermission('demand_in_view')")
    public R<DemandInResponse> detail(@RequestBody IdRequest request) {
        return R.ok(demandInService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除")
    //@PreAuthorize("@pms.hasPermission('demand_in_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(demandInService.removeByIds(request.getIds()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('demand_in_view')")
    public R<IPage<DemandInResponse>> page(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject DemandInPageRequest request) {
        return R.ok(demandInService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('demand_in_export')")
    public R<ExportFieldListResponse> exportFields() {
        return R.ok(ExportFieldHelper.buildExportFieldList(
                DemandInResponse.BIZ_CODE,
                DemandInResponse.class
        ));
    }

    @PostMapping("/export")
    @Operation(summary = "导出需求记录")
    public void export(@RequestBody DemandInExportWrapperRequest request) throws IOException {
        // 1. 拿到 ServletRequestAttributes
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }

        // 2. 再拿到 Jakarta HttpServletResponse
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 3. 查询数据
        IPage<DemandInResponse> pageData = demandInService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "校内需求导出",
                // Sheet 名称
                "校内需求",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                DemandInResponse.class
        );
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架")
    //@PreAuthorize("@pms.hasPermission('demand_in_edit')")
    public R<Boolean> updateShelfStatus(@RequestBody ShelfStatusRequest request) {
        return R.ok(demandInService.updateShelfStatus(request.getId(), request.getShelfStatus()));
    }

}