package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.HttpHeaders;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.*;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patentProposal")
@Tag(description = "专利提案表管理", name = "专利提案表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentProposalController {

    private final PatentProposalService patentProposalService;

    @GetMapping("/page")
    @Operation(summary = "分页查询专利提案")
    //@PreAuthorize("@pms.hasPermission('patent_proposal_view')")
    public R<IPage<PatentProposalResponse>> page(@ParameterObject PageRequest pageRequest, @ParameterObject PatentProposalPageRequest request) {
        return R.ok(patentProposalService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "专利提案详情")
    //@PreAuthorize("@pms.hasPermission('patent_proposal_view')")
    public R<PatentProposalResponse> detail(@RequestBody IdRequest request) {
        return R.ok(patentProposalService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增专利提案")
    @SysLog("新增专利提案")
    //@PreAuthorize("@pms.hasPermission('patent_proposal_add')")
    public R<Boolean> create(@RequestBody @Valid PatentProposalCreateRequest request) {
        return R.ok(patentProposalService.createProposal(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改专利提案")
    @SysLog("修改专利提案")
    //@PreAuthorize("@pms.hasPermission('patent_proposal_edit')")
    public R<Boolean> update(@RequestBody PatentProposalUpdateRequest request) {
        return R.ok(patentProposalService.updateProposal(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除专利提案")
    @SysLog("删除专利提案")
    //@PreAuthorize("@pms.hasPermission('patent_proposal_del')")
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
    //@PreAuthorize("@pms.hasPermission('patent_proposal_export')")
    public void export(@RequestBody PatentProposalExportWrapperRequest request) throws IOException {
        // 1. 拿到 ServletRequestAttributes
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }

        // 2. 再拿到 Jakarta HttpServletResponse
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 3. 查询数据
        IPage<PatentProposalResponse> pageData = patentProposalService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "专利提案导出",
                // Sheet 名称
                "专利提案",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                PatentProposalResponse.class
        );
    }
}
