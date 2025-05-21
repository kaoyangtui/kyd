package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.*;
import com.pig4cloud.pigx.admin.service.ResearchTeamService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/researchTeam")
@Tag(name = "科研团队管理", description = "科研团队管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchTeamController {

    private final ResearchTeamService researchTeamService;

    @PostMapping("/create")
    @Operation(summary = "新增科研团队")
    @PreAuthorize("@pms.hasPermission('researchTeam_add')")
    public R<ResearchTeamResponse> create(@RequestBody ResearchTeamCreateRequest request) {
        return R.ok(researchTeamService.createTeam(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新科研团队")
    @PreAuthorize("@pms.hasPermission('researchTeam_edit')")
    public R<Boolean> update(@RequestBody ResearchTeamUpdateRequest request) {
        return R.ok(researchTeamService.updateTeam(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "科研团队详情")
    @PreAuthorize("@pms.hasPermission('researchTeam_view')")
    public R<ResearchTeamResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchTeamService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @SysLog("删除科研团队")
    @Operation(summary = "删除科研团队")
    @PreAuthorize("@pms.hasPermission('researchTeam_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchTeamService.removeTeam(request));
    }

    @PostMapping("/shelf")
    @Operation(summary = "科研团队上下架")
    @PreAuthorize("@pms.hasPermission('researchTeam_edit')")
    public R<Boolean> shelf(@RequestBody ResearchTeamShelfRequest request) {
        return R.ok(researchTeamService.updateShelfStatus(request));
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询科研团队")
    @PreAuthorize("@pms.hasPermission('researchTeam_view')")
    public R<IPage<ResearchTeamResponse>> page(@ParameterObject Page page, @ParameterObject ResearchTeamPageRequest request) {
        return R.ok(researchTeamService.pageResult(page, request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResearchTeamResponse.BIZ_CODE,
                ResearchTeamResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "科研团队导出", sheets = {@Sheet(sheetName = "科研团队列表")})
    @Operation(summary = "导出科研团队")
    @PreAuthorize("@pms.hasPermission('researchTeam_export')")
    public List<Map<String, Object>> export(@RequestBody ResearchTeamExportWrapperRequest request) {
        IPage<ResearchTeamResponse> pageData = researchTeamService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), ResearchTeamResponse.class);
    }
}
