package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.*;
import com.pig4cloud.pigx.admin.service.SoftCopyRegService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
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
@RequestMapping("/softCopyReg")
@Tag(name = "软著登记管理", description = "软著登记管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SoftCopyRegController {

    private final SoftCopyRegService softCopyRegService;

    @PostMapping("/create")
    @Operation(summary = "新增软著登记")
    @SysLog("新增软著登记")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_add')")
    public R<Boolean> create(@RequestBody SoftCopyRegCreateRequest request) {
        return R.ok(softCopyRegService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改软著登记")
    @SysLog("修改软著登记")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_edit')")
    public R<Boolean> update(@RequestBody SoftCopyRegUpdateRequest request) {
        return R.ok(softCopyRegService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询软著登记")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_view')")
    public R<IPage<SoftCopyRegResponse>> page(@ParameterObject PageRequest pageRequest,
                                              @ParameterObject SoftCopyRegPageRequest request) {
        return R.ok(softCopyRegService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询软著登记详情")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_view')")
    public R<SoftCopyRegResponse> detail(@RequestBody IdRequest request) {
        return R.ok(softCopyRegService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除软著登记")
    @SysLog("删除软著登记")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(softCopyRegService.remove(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                SoftCopyRegResponse.BIZ_CODE,
                SoftCopyRegResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "软著登记导出", sheets = {@Sheet(sheetName = "软著登记列表")})
    @Operation(summary = "导出软著登记")
    //@PreAuthorize("@pms.hasPermission('soft_copy_reg_export')")
    public void export(@RequestBody SoftCopyRegExportWrapperRequest request) throws IOException {
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
        IPage<SoftCopyRegResponse> pageData = softCopyRegService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "软著登记导出",
                // Sheet 名称
                "软著登记",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                SoftCopyRegResponse.class
        );
    }
}
