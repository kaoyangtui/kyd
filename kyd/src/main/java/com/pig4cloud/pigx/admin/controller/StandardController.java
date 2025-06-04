package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.standard.*;
import com.pig4cloud.pigx.admin.service.StandardService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.utils.PageUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/standard")
@Tag(name = "标准信息管理", description = "标准信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class StandardController {

    private final StandardService standardService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('standard_view')")
    public R<IPage<StandardResponse>> page(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject StandardPageRequest request) {
        return R.ok(standardService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    //@PreAuthorize("@pms.hasPermission('standard_view')")
    public R<StandardResponse> detail(@RequestBody IdRequest request) {
        return R.ok(standardService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增标准信息")
    //@PreAuthorize("@pms.hasPermission('standard_add')")
    public R<Boolean> create(@RequestBody StandardCreateRequest request) {
        return R.ok(standardService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改标准信息")
    //@PreAuthorize("@pms.hasPermission('standard_edit')")
    public R<Boolean> update(@RequestBody StandardUpdateRequest request) {
        return R.ok(standardService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除标准信息")
    //@PreAuthorize("@pms.hasPermission('standard_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(standardService.removeByIds(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('standard_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                StandardResponse.BIZ_CODE,
                StandardResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "标准信息导出", sheets = {@Sheet(sheetName = "标准信息列表")})
    @Operation(summary = "导出")
    //@PreAuthorize("@pms.hasPermission('standard_export')")
    public void export(@RequestBody StandardExportWrapperRequest request) throws IOException {
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
        IPage<StandardResponse> pageData = standardService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "标准信息导出",
                // Sheet 名称
                "标准信息",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                StandardResponse.class
        );
    }
}
