package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.notice.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.NoticeService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "通知公告管理", description = "通知公告管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<NoticeResponse>> page(@ParameterObject Page<?> page, @ParameterObject NoticePageRequest request) {
        return R.ok(noticeService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<NoticeResponse> detail(@RequestBody IdRequest request) {
        return R.ok(noticeService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增通知公告")
    public R<Boolean> create(@RequestBody NoticeCreateRequest request) {
        return R.ok(noticeService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改通知公告")
    public R<Boolean> update(@RequestBody NoticeUpdateRequest request) {
        return R.ok(noticeService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除通知公告")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(noticeService.remove(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        return R.ok(ExportFieldHelper.buildExportFieldList(
                NoticeResponse.BIZ_CODE,
                NoticeResponse.class
        ));
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ExportWrapperRequest<NoticePageRequest> request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<NoticeResponse> pageData = noticeService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "通知公告导出",
                "通知公告列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                NoticeResponse.class
        );
    }
}
