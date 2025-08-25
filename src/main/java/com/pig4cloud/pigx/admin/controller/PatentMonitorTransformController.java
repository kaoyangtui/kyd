package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.HttpHeaders;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformResponse;
import com.pig4cloud.pigx.admin.service.PatentMonitorTransformService;
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
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/patentMonitorTransform")
@RequiredArgsConstructor
@Tag(description = "转化专利监控管理", name = "转化专利监控管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentMonitorTransformController {

    private final PatentMonitorTransformService patentMonitorTransformService;

    @GetMapping("/page")
    @Operation(summary = "分页查询转化监控列表")
    public R<IPage<PatentMonitorTransformResponse>> page(
            @ParameterObject PageRequest pageRequest,
            @ParameterObject PatentMonitorTransformPageRequest request) {
        return R.ok(patentMonitorTransformService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量逻辑删除")
    public R<Boolean> remove(@RequestBody List<Long> idList) {
        return R.ok(patentMonitorTransformService.remove(idList));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentMonitorTransformResponse.BIZ_CODE,
                PatentMonitorTransformResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "专利转化监控导出", sheets = {@Sheet(sheetName = "监控列表")})
    @Operation(summary = "导出专利转化监控列表")
    public void export(@RequestBody PatentMonitorTransformExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<PatentMonitorTransformResponse> pageData = patentMonitorTransformService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "专利转化监控导出",
                "监控列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                PatentMonitorTransformResponse.class
        );
    }
}
