package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.consult.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.ConsultService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consult")
@Tag(name = "咨询信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ConsultController {

    private final ConsultService consultService;

    @PostMapping("/create")
    @Operation(summary = "新增咨询")
    public R<Boolean> create(@RequestBody @Valid ConsultCreateRequest request) {
        return R.ok(consultService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新咨询")
    public R<Boolean> update(@RequestBody ConsultUpdateRequest request) {
        return R.ok(consultService.update(request));
    }

    @PostMapping("/reply")
    @Operation(summary = "回复咨询")
    public R<Boolean> reply(@RequestBody ConsultReplyRequest request) {
        return R.ok(consultService.reply(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询咨询信息")
    public R<IPage<ConsultResponse>> page(@ParameterObject PageRequest pageRequest,
                                          @ParameterObject ConsultPageRequest request) {
        return R.ok(consultService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "获取咨询详情")
    public R<ConsultResponse> detail(@RequestBody IdRequest request) {
        return R.ok(consultService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除咨询信息")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(consultService.removeByIds(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(ConsultResponse.BIZ_CODE);
        response.setFields(ExportFieldHelper.getFieldsFromDto(ConsultResponse.class));
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "咨询信息导出", sheets = {@Sheet(sheetName = "咨询列表")})
    @Operation(summary = "导出咨询信息")
    public List<Map<String, Object>> export(@RequestBody ConsultExportWrapperRequest request) {
        IPage<ConsultResponse> pageData = consultService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), ConsultResponse.class);
    }
}