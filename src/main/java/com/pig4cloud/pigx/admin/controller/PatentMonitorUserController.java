package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.HttpHeaders;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserResponse;
import com.pig4cloud.pigx.admin.service.PatentMonitorUserService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/patentMonitorUser")
@Tag(description = "用户专利监控管理", name = "用户专利监控管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentMonitorUserController {

    private final PatentMonitorUserService patentMonitorUserService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户监控的专利列表")
    public R<IPage<PatentMonitorUserResponse>> page(
             @ParameterObject PageRequest pageRequest,
             @ParameterObject PatentMonitorUserPageRequest request) {
        return R.ok(patentMonitorUserService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/create")
    @Operation(summary = "批量添加监控")
    public R<Boolean> create(@RequestBody List<String> pidList) {
        return R.ok(patentMonitorUserService.create(pidList));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量取消监控")
    public R<Boolean> remove(@RequestBody List<String> pidList) {
        return R.ok(patentMonitorUserService.remove(pidList));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentMonitorUserResponse.BIZ_CODE,
                PatentMonitorUserResponse.class
        );
        return R.ok(fields);
    }


    @PostMapping("/export")
    @Operation(summary = "导出专利监控列表")
    public void export(@RequestBody PatentMonitorUserExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 查询全部（或按你通用封装处理分页/ids/range等方式）
        IPage<PatentMonitorUserResponse> pageData = patentMonitorUserService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 导出
        ExcelExportUtil.exportByBean(
                response,
                "专利监控导出",
                "监控列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                PatentMonitorUserResponse.class
        );
    }

}
