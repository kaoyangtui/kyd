package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.perf.*;
import com.pig4cloud.pigx.admin.service.PerfRuleResultService;
import com.pig4cloud.pigx.admin.service.PerfSchemeService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/perf/scheme")
@Tag(description = "业绩点方案", name = "业绩点方案")
@RequiredArgsConstructor
public class PerfSchemeController {

    private final PerfSchemeService perfSchemeService;
    private final PerfRuleResultService perfRuleResultService;

    @PostMapping("/save")
    @Operation(summary = "保存方案及规则（一起提交）")
    public R<Boolean> save(@RequestBody PerfSchemeSaveRequest request) {
        return R.ok(perfSchemeService.doSaveOrUpdateWithRules(request));
    }

    @GetMapping("/detail")
    @Operation(summary = "方案含规则详情")
    public R<PerfSchemeWithRulesResponse> detailWithRules(@RequestParam("id") Long id) {
        return R.ok(perfSchemeService.detailWithRules(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询业绩点方案")
    public R<IPage<PerfSchemeResponse>> page(@ParameterObject PageRequest pageRequest,
                                             @ParameterObject PerfSchemePageRequest request) {
        return R.ok(perfSchemeService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除方案")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.ok(perfSchemeService.remove(ids));
    }

    @PostMapping("/shelf")
    @Operation(summary = "启停方案")
    public R<Boolean> shelf(@RequestBody Long id) {
        return R.ok(perfSchemeService.toggleStatus(id));
    }

    @GetMapping("/overview")
    @Operation(summary = "按方案查询总览（总分/统计期/单元格分数与件数）")
    public R<PerfSchemeOverviewResponse> overview(@RequestParam("schemeId") Long schemeId) {
        return R.ok(perfSchemeService.overview(schemeId));
    }

    @PostMapping("/run")
    @Operation(summary = "执行业绩计算")
    public R<PerfRuleCalcSummary> run(@RequestBody PerfRuleCalcRequest req) {
        return R.ok(perfRuleResultService.runCalc(req));
    }

    // ====== 方案导出字段 ======
    @PostMapping("/export/fields")
    @Operation(summary = "获取方案导出字段列表")
    public R<ExportFieldListResponse> schemeExportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PerfSchemeResponse.BIZ_CODE,
                PerfSchemeResponse.class
        );
        return R.ok(fields);
    }

    // ====== 方案导出 ======
    @PostMapping("/export")
    @Operation(summary = "导出方案")
    public void exportScheme(@RequestBody PerfSchemeExportWrapperRequest request) throws IOException {
        // 1) 取响应对象
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 2) 查数据（导出模式 forExport=true：全量）
        IPage<PerfSchemeResponse> pageData = perfSchemeService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 3) 通用导出
        ExcelExportUtil.exportByBean(
                response,
                "业绩点方案导出",
                "业绩点方案",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                PerfSchemeResponse.class
        );
    }
}
