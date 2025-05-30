package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.researchProject.*;
import com.pig4cloud.pigx.admin.service.ResearchProjectService;
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
@RequestMapping("/researchProject")
@Tag(name = "科研项目管理", description = "科研项目信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchProjectController {

    private final ResearchProjectService researchProjectService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ResearchProjectResponse>> page(@ParameterObject Page<?> page, @ParameterObject ResearchProjectPageRequest request) {
        return R.ok(researchProjectService.pageResult(page, request));
    }


    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ResearchProjectResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchProjectService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增科研项目")
    public R<Boolean> create(@RequestBody ResearchProjectCreateRequest request) {
        return R.ok(researchProjectService.createProject(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改科研项目")
    public R<Boolean> update(@RequestBody ResearchProjectUpdateRequest request) {
        return R.ok(researchProjectService.updateProject(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除科研项目")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchProjectService.removeProjects(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResearchProjectResponse.BIZ_CODE,
                ResearchProjectResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ResearchProjectExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        HttpServletResponse response = attrs.getResponse();
        if (response == null) throw new IllegalStateException("无法获取 HttpServletResponse");

        IPage<ResearchProjectResponse> pageData = researchProjectService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "科研项目导出",
                "科研项目列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ResearchProjectResponse.class
        );
    }

    @PostMapping("/type/options")
    @Operation(summary = "项目类型下拉模糊搜索")
    public R<List<String>> projectTypeOptions(@RequestBody ProjectTypeSearchRequest request) {
        return R.ok(researchProjectService.projectTypeOptions(request));
    }

}
