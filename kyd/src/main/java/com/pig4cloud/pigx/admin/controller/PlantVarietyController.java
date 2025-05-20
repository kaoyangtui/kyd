package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.service.PlantVarietyService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.ExportFilterUtil;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldResponse;
import com.pig4cloud.pigx.admin.vo.IdListRequest;
import com.pig4cloud.pigx.admin.vo.IdRequest;
import com.pig4cloud.pigx.admin.vo.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.vo.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.vo.plantVariety.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/plantVariety")
@Tag(name = "植物新品种权管理")
public class PlantVarietyController {

    private final PlantVarietyService plantVarietyService;

    @GetMapping("/page")
    @Operation(summary = "分页查询植物新品种权")
    @PreAuthorize("@pms.hasPermission('plant_variety_view')")
    public R<IPage<PlantVarietyResponse>> page(@ParameterObject Page page, @ParameterObject PlantVarietyPageRequest request) {
        return R.ok(plantVarietyService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查询详情")
    @PreAuthorize("@pms.hasPermission('plant_variety_view')")
    public R<PlantVarietyResponse> detail(@RequestBody IdRequest request) {
        return R.ok(plantVarietyService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增植物新品种权")
    @SysLog("新增植物新品种权")
    @PreAuthorize("@pms.hasPermission('plant_variety_add')")
    public R<Boolean> create(@RequestBody PlantVarietyCreateRequest request) {
        return R.ok(plantVarietyService.createVariety(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改植物新品种权")
    @SysLog("修改植物新品种权")
    @PreAuthorize("@pms.hasPermission('plant_variety_edit')")
    public R<Boolean> update(@RequestBody PlantVarietyUpdateRequest request) {
        return R.ok(plantVarietyService.updateVariety(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除植物新品种权")
    @SysLog("删除植物新品种权")
    @PreAuthorize("@pms.hasPermission('plant_variety_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(plantVarietyService.removeVarieties(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    @PreAuthorize("@pms.hasPermission('plant_variety_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                IpAssignResponse.BIZ_CODE,
                PlantVarietyVO.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "植物新品种权导出", sheets = {@Sheet(sheetName = "品种列表")})
    @Operation(summary = "导出植物新品种权")
    @PreAuthorize("@pms.hasPermission('plant_variety_export')")
    public List<Map<String, Object>> export(@RequestBody PlantVarietyExportWrapperRequest request) {
        IPage<PlantVarietyResponse> pageData = plantVarietyService.pageResult(new Page<>(), request.getQuery());
        return ExportFilterUtil.filterFields(pageData.getRecords(), request.getExport().getFieldKeys(), PlantVarietyResponse.class);
    }

}
