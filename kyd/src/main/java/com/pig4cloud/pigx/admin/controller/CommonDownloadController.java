package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.commonDownload.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.CommonDownloadService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commonDownload")
@Tag(name = "常用下载管理", description = "常用下载管理")
public class CommonDownloadController {

    private final CommonDownloadService commonDownloadService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<CommonDownloadResponse>> page(@ParameterObject PageRequest pageRequest,
                                                 @ParameterObject CommonDownloadPageRequest request) {
        return R.ok(commonDownloadService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<CommonDownloadResponse> detail(@RequestBody IdRequest request) {
        return R.ok(commonDownloadService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增常用下载")
    public R<Boolean> create(@RequestBody @Valid CommonDownloadCreateRequest request) {
        return R.ok(commonDownloadService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改常用下载")
    public R<Boolean> update(@RequestBody CommonDownloadUpdateRequest request) {
        return R.ok(commonDownloadService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除常用下载")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(commonDownloadService.remove(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                CommonDownloadResponse.BIZ_CODE,
                CommonDownloadResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody CommonDownloadExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        HttpServletResponse response = attrs.getResponse();
        if (response == null) throw new IllegalStateException("无法获取 HttpServletResponse");

        IPage<CommonDownloadResponse> pageData = commonDownloadService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "常用下载导出",
                "常用下载列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                CommonDownloadResponse.class
        );
    }
}
