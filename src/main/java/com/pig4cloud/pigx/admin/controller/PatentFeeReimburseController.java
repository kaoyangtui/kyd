package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patentFee.*;
import com.pig4cloud.pigx.admin.service.PatentFeeReimburseService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patentFeeReimburse")
@Tag(description = "专利费用报销管理", name = "专利费用报销管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentFeeReimburseController {

    private final PatentFeeReimburseService patentFeeReimburseService;

    @PostMapping("/create")
    @Operation(summary = "新增专利费用报销")
    public R<PatentFeeReimburseResponse> create(@RequestBody @Valid PatentFeeReimburseCreateRequest request) {
        return R.ok(patentFeeReimburseService.createPatentFeeReimburse(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新专利费用报销")
    public R<Boolean> update(@RequestBody PatentFeeReimburseUpdateRequest request) {
        return R.ok(patentFeeReimburseService.updatePatentFeeReimburse(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询专利费用报销")
    public R<IPage<PatentFeeReimburseResponse>> page(@ParameterObject PageRequest pageRequest,
                                                     @ParameterObject PatentFeeReimbursePageRequest request) {
        return R.ok(patentFeeReimburseService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询专利费用报销详情")
    public R<PatentFeeReimburseResponse> detail(@RequestBody IdRequest request) {
        return R.ok(patentFeeReimburseService.getDetail(request.getId()));
    }


    @PostMapping("/remove")
    @Operation(summary = "删除专利费用报销")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(patentFeeReimburseService.removePatentFeeReimburse(request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentFeeReimburseResponse.BIZ_CODE,
                PatentFeeReimburseResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出专利费用报销")
    public void export(@RequestBody PatentFeeReimburseExportWrapperRequest request) throws IOException {
        // 1. 获取当前请求上下文
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }

        // 2. 获取 HttpServletResponse
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        // 3. 查询全部数据（或自定义分页策略）
        IPage<PatentFeeReimburseResponse> pageData = patentFeeReimburseService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 导出 Excel
        ExcelExportUtil.exportByBean(
                response,
                "专利费用报销导出",        // 文件名（不带.xlsx）
                "专利费用报销列表",        // Sheet名称
                pageData.getRecords(),     // DTO列表
                request.getExport().getFieldKeys(), // 导出字段key列表
                PatentFeeReimburseResponse.class    // DTO类型
        );
    }

}
