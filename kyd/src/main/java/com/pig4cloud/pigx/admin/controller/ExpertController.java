package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.expert.*;
import com.pig4cloud.pigx.admin.service.ExpertService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
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

/**
 * 专家信息管理
 *
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/expert")
@Tag(name = "专家管理", description = "专家信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExpertController {

    private final ExpertService expertService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ExpertResponse>> page(@ParameterObject PageRequest pageRequest,
                                         @RequestBody ExpertPageRequest request) {
        return R.ok(expertService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ExpertResponse> detail(@RequestBody IdRequest request) {
        return R.ok(expertService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增专家信息")
    public R<Boolean> create(@RequestBody ExpertCreateRequest request) {
        return R.ok(expertService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改专家信息")
    public R<Boolean> update(@RequestBody ExpertUpdateRequest request) {
        return R.ok(expertService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除")
    @SysLog("批量删除专家")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(expertService.remove(request.getIds()));
    }

    @PostMapping("/shelf")
    @Operation(summary = "批量上下架")
    @SysLog("专家上下架")
    public R<Boolean> shelf(@RequestBody ExpertShelfRequest request) {
        return R.ok(expertService.shelfByIds(request.getIds(), request.getShelfStatus()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ExpertResponse.BIZ_CODE, ExpertResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ExpertExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<ExpertResponse> pageData = expertService.pageResult(new Page<>(), request.getQuery());

        ExcelExportUtil.exportByBean(
                response,
                "专家信息导出",
                "专家信息列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ExpertResponse.class
        );
    }
}
