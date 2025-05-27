package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.HttpHeaders;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/research-platform")
@Tag(name = "科研平台管理", description = "科研平台管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchPlatformController {

    private final ResearchPlatformService researchPlatformService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ResearchPlatformResponse>> page(@ParameterObject Page page,
                                                   @RequestBody ResearchPlatformPageRequest request) {
        return R.ok(researchPlatformService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ResearchPlatformResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchPlatformService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增科研平台")
    public R<Boolean> create(@RequestBody ResearchPlatformCreateRequest request) {
        return R.ok(researchPlatformService.createPlatform(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改科研平台")
    public R<Boolean> update(@RequestBody ResearchPlatformUpdateRequest request) {
        return R.ok(researchPlatformService.updatePlatform(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除科研平台")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchPlatformService.removePlatforms(request.getIds()));
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架操作")
    @SysLog("科研平台上下架")
    public R<Boolean> shelf(@RequestBody ResearchPlatformShelfRequest request) {
        return R.ok(researchPlatformService.changeShelfStatus(request));
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
        HttpServletResponse response = attrs.getResponse();

        IPage<ResearchPlatformResponse> pageData = researchPlatformService.pageResult(
                new Page<>(), request.getQuery()
        );

        ExcelExportUtil.exportByBean(
                response,
                "科研平台列表",
                "科研平台数据",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ResearchPlatformResponse.class
        );
    }
}