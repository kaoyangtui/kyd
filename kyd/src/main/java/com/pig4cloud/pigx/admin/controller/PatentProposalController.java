package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专利提案表
 *
 * @author pigx
 * @date 2025-05-11 17:08:56
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patentProposal" )
@Tag(description = "patentProposal" , name = "专利提案表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentProposalController {

    private final  PatentProposalService patentProposalService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_view')")
    public R<IPage<PatentProposalResponse>> page(@RequestBody PatentProposalPageRequest request) {
        return R.ok(patentProposalService.pageResult(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_view')")
    public R<PatentProposalResponse> detail(@RequestBody IdRequest request) {
        return R.ok(patentProposalService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_add')")
    public R<Boolean> create(@RequestBody PatentProposalCreateRequest request) {
        return R.ok(patentProposalService.createProposal(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_edit')")
    public R<Boolean> update(@RequestBody PatentProposalUpdateRequest request) {
        return R.ok(patentProposalService.updateProposal(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除专利提案")
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(patentProposalService.removeProposals(request.getIds()));
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    @ResponseExcel(name = "专利提案", sheets = {@Sheet(sheetName = "提案列表")})
    @PreAuthorize("@pms.hasPermission('admin_patent_proposal_export')")
    public List<PatentProposalResponse> export(@RequestBody PatentProposalPageRequest request) {
        return patentProposalService.exportList(request);
    }
}