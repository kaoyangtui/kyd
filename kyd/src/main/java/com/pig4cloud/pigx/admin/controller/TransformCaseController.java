package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.*;
import com.pig4cloud.pigx.admin.service.TransformCaseService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.core.util.R;
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

/**
 * 转化案例管理
 *
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/transformCase")
@Tag(name = "转化案例管理", description = "转化案例管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TransformCaseController {

    private final TransformCaseService transformCaseService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<TransformCaseResponse>> page(@ParameterObject Page<?> page, @ParameterObject TransformCasePageRequest request) {
        return R.ok(transformCaseService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<TransformCaseResponse> detail(@RequestBody IdRequest request) {
        return R.ok(transformCaseService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增转化案例")
    public R<Boolean> create(@RequestBody TransformCaseCreateRequest request) {
        return R.ok(transformCaseService.createCase(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改转化案例")
    public R<Boolean> update(@RequestBody TransformCaseUpdateRequest request) {
        return R.ok(transformCaseService.updateCase(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除转化案例")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(transformCaseService.removeCases(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                TransformCaseResponse.BIZ_CODE,
                TransformCaseResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody TransformCaseExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<TransformCaseResponse> pageData = transformCaseService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "转化案例导出",
                "转化案例列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                TransformCaseResponse.class
        );
    }
}
