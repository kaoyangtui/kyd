package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchPlatform.*;
import com.pig4cloud.pigx.admin.service.ResearchPlatformService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchPlatform")
@Tag(name = "科研平台管理", description = "科研平台管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchPlatformController {

    private final ResearchPlatformService researchPlatformService;

    @PostMapping("/create")
    @Operation(summary = "新增科研平台")
    @SysLog("新增科研平台")
    public R<Boolean> create(@RequestBody ResearchPlatformCreateRequest request) {
        return R.ok(researchPlatformService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改科研平台")
    @SysLog("修改科研平台")
    public R<Boolean> update(@RequestBody ResearchPlatformUpdateRequest request) {
        return R.ok(researchPlatformService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除科研平台")
    @SysLog("删除科研平台")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchPlatformService.remove(request.getIds()));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询详情")
    public R<ResearchPlatformResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchPlatformService.getDetail(request.getId()));
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架")
    public R<Boolean> shelf(@RequestBody ResearchPlatformShelfRequest request) {
        return R.ok(researchPlatformService.shelfByIds(request.getIds(), request.getShelfStatus()));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ResearchPlatformResponse>> page(@RequestBody ResearchPlatformPageRequest request) {
        return R.ok(researchPlatformService.pageResult(new Page<>(), request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResearchPlatformResponse.BIZ_CODE,
                ResearchPlatformResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ResearchPlatformExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        HttpServletResponse response = attrs.getResponse();
        if (response == null) throw new IllegalStateException("无法获取 HttpServletResponse");

        IPage<ResearchPlatformResponse> pageData = researchPlatformService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "科研平台导出",
                "科研平台列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ResearchPlatformResponse.class
        );
    }
}
