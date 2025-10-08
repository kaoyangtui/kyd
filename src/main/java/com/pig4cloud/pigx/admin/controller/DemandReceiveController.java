package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceiveExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.DemandReceiveService;
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

/**
 * 企业需求接收表
 *
 * @author pigx
 * @date 2025-09-25 11:57:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/demandReceive")
@Tag(description = "demandReceive", name = "企业需求接收表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DemandReceiveController {

    private final DemandReceiveService demandReceiveService;


    @GetMapping("/page")
    @Operation(summary = "分页查询需求接收信息")
    public R<IPage<DemandReceiveResponse>> receivePage(@ParameterObject PageRequest pageRequest,
                                                       @ParameterObject DemandReceivePageRequest request) {
        return R.ok(demandReceiveService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @Operation(summary = "标记已读（支持批量）")
    @PostMapping("/read")
    public R<Integer> read(@Valid @RequestBody IdListRequest req) {
        int rows = demandReceiveService.markReadBatch(req.getIds());
        return R.ok(rows);
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(DemandReceiveResponse.BIZ_CODE);
        response.setFields(ExportFieldHelper.getFieldsFromDto(DemandReceiveResponse.class));
        return R.ok(response);
    }

    @PostMapping("/export")
    @Operation(summary = "导出企业需求接收")
    //@PreAuthorize("@pms.hasPermission('demand_export')")
    public void export(@RequestBody DemandReceiveExportWrapperRequest request) throws IOException {
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
        IPage<DemandReceiveResponse> pageData = demandReceiveService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "企业需求接收",
                // Sheet 名称
                "企业需求接收",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                DemandReceiveResponse.class
        );
    }
}