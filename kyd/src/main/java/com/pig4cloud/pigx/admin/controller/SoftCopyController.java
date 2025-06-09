package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.softCopy.*;
import com.pig4cloud.pigx.admin.service.SoftCopyService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
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

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('soft_copy_view')")
    public R<IPage<SoftCopyResponse>> page(@ParameterObject PageRequest pageRequest,
                                           @ParameterObject SoftCopyPageRequest request) {
        return R.ok(softCopyService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    //@PreAuthorize("@pms.hasPermission('soft_copy_view')")
    public R<SoftCopyResponse> detail(@RequestBody IdRequest request) {
        return R.ok(softCopyService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增软著提案")
    //@PreAuthorize("@pms.hasPermission('soft_copy_add')")
    public R<Boolean> create(@RequestBody @Valid SoftCopyCreateRequest request) {
        return R.ok(softCopyService.createProposal(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改软著提案")
    //@PreAuthorize("@pms.hasPermission('soft_copy_edit')")
    public R<Boolean> update(@RequestBody SoftCopyUpdateRequest request) {
        return R.ok(softCopyService.updateProposal(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除软著提案")
    //@PreAuthorize("@pms.hasPermission('soft_copy_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(softCopyService.removeProposals(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('soft_copy_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                SoftCopyResponse.BIZ_CODE,
                SoftCopyResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody SoftCopyExportWrapperRequest request) throws IOException {
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
        IPage<SoftCopyResponse> pageData = softCopyService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "软著提案导出",
                // Sheet 名称
                "软著提案列表",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                SoftCopyResponse.class
        );
    }

}
