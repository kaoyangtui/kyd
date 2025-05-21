package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.HttpHeaders;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.dto.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patentProposal")
@Tag(description = "专利提案表管理", name = "专利提案表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentProposalController {

    private final PatentProposalService patentProposalService;

    @GetMapping("/page")
    @Operation(summary = "分页查询专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_view')")
    public R<IPage<PatentProposalResponse>> page(@ParameterObject Page page, @ParameterObject PatentProposalPageRequest request) {
        return R.ok(patentProposalService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "专利提案详情")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_view')")
    public R<PatentProposalResponse> detail(@RequestBody IdRequest request) {
        return R.ok(patentProposalService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增专利提案")
    @SysLog("新增专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_add')")
    public R<Boolean> create(@RequestBody PatentProposalCreateRequest request) {
        return R.ok(patentProposalService.createProposal(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改专利提案")
    @SysLog("修改专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_edit')")
    public R<Boolean> update(@RequestBody PatentProposalUpdateRequest request) {
        return R.ok(patentProposalService.updateProposal(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除专利提案")
    @SysLog("删除专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(patentProposalService.removeProposals(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取专利提案导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentProposalResponse.BIZ_CODE,
                PatentProposalResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "专利提案导出", sheets = {@Sheet(sheetName = "提案列表")})
    @Operation(summary = "导出专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_export')")
    public List<Map<String, Object>> export(@RequestBody PatentProposalExportWrapperRequest request) {
        IPage<PatentProposalResponse> pageData = patentProposalService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), PatentProposalResponse.class);
    }
}
