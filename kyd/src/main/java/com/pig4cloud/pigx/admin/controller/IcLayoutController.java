package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.icLayout.*;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyResponse;
import com.pig4cloud.pigx.admin.service.IcLayoutService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/icLayout")
@Tag(name = "集成电路布图登记", description = "集成电路布图登记")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class IcLayoutController {

    private final IcLayoutService icLayoutService;

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增集成电路布图登记")
    //@PreAuthorize("@pms.hasPermission('ic_layout_add')")
    public R<Boolean> create(@RequestBody IcLayoutCreateRequest request) {
        return R.ok(icLayoutService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改集成电路布图登记")
    //@PreAuthorize("@pms.hasPermission('ic_layout_edit')")
    public R<Boolean> update(@RequestBody IcLayoutUpdateRequest request) {
        return R.ok(icLayoutService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除集成电路布图登记")
    //@PreAuthorize("@pms.hasPermission('ic_layout_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(icLayoutService.remove(request.getIds()));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    //@PreAuthorize("@pms.hasPermission('ic_layout_view')")
    public R<IcLayoutResponse> detail(@RequestBody IdRequest request) {
        return R.ok(icLayoutService.getDetail(request.getId()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('ic_layout_view')")
    public R<IPage<IcLayoutResponse>> page(@ParameterObject Page page, @ParameterObject IcLayoutPageRequest request) {
        return R.ok(icLayoutService.pageResult(page, request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('ic_layout_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                IcLayoutResponse.BIZ_CODE,
                IcLayoutResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "集成电路布图登记导出", sheets = {@Sheet(sheetName = "集成电路布图登记")})
    @Operation(summary = "导出")
    //@PreAuthorize("@pms.hasPermission('ic_layout_export')")
    public void export(@RequestBody IcLayoutExportWrapperRequest request) throws IOException {
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
        IPage<IcLayoutResponse> pageData = icLayoutService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "集成电路布图登记导出",
                // Sheet 名称
                "集成电路布图登记",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                IcLayoutResponse.class
        );
    }
}