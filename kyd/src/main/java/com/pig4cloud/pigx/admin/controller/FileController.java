package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutResponse;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.file.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
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
@RequestMapping("/file")
@Tag(name = "文档管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FileController {

    private final FileService fileService;

    @PostMapping("/create")
    @Operation(summary = "新增文档")
    //@PreAuthorize("@pms.hasPermission('file_add')")
    public R<Boolean> create(@RequestBody FileCreateRequest request) {
        return R.ok(fileService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新文档")
    //@PreAuthorize("@pms.hasPermission('file_edit')")
    public R<Boolean> update(@RequestBody FileUpdateRequest request) {
        return R.ok(fileService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询文档")
    //@PreAuthorize("@pms.hasPermission('file_view')")
    public R<IPage<FileResponse>> page(@ParameterObject Page page, @ParameterObject FilePageRequest request) {
        return R.ok(fileService.pageResult(page, request));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除文档")
    //@PreAuthorize("@pms.hasPermission('file_del')")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.ok(fileService.removeByIds(ids));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = ExportFieldHelper.buildExportFieldList(
                FileResponse.BIZ_CODE,
                FileResponse.class
        );
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "文档导出", sheets = {@Sheet(sheetName = "文档列表")})
    @Operation(summary = "导出文档")
    //@PreAuthorize("@pms.hasPermission('file_export')")
    public void export(@RequestBody FileExportWrapperRequest request) throws IOException {
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
        IPage<FileResponse> pageData = fileService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "文档导出",
                // Sheet 名称
                "文档",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                FileResponse.class
        );
    }
}
