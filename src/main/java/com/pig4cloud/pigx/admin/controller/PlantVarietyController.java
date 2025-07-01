package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.plantVariety.*;
import com.pig4cloud.pigx.admin.service.PlantVarietyService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plantVariety")
@Tag(name = "植物新品种权登记管理", description = "植物新品种权登记管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PlantVarietyController {

    private final PlantVarietyService plantVarietyService;

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增植物新品种权")
    //@PreAuthorize("@pms.hasPermission('plant_variety_add')")
    public R<Boolean> create(@RequestBody @Valid PlantVarietyCreateRequest request) {
        return R.ok(plantVarietyService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新")
    @SysLog("更新植物新品种权")
    //@PreAuthorize("@pms.hasPermission('plant_variety_edit')")
    public R<Boolean> update(@RequestBody PlantVarietyUpdateRequest request) {
        return R.ok(plantVarietyService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('plant_variety_view')")
    public R<IPage<PlantVarietyResponse>> page(@ParameterObject PageRequest pageRequest, @ParameterObject PlantVarietyPageRequest request) {
        return R.ok(plantVarietyService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    //@PreAuthorize("@pms.hasPermission('plant_variety_view')")
    public R<PlantVarietyResponse> detail(@RequestBody IdRequest request) {
        return R.ok(plantVarietyService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除植物新品种权")
    //@PreAuthorize("@pms.hasPermission('plant_variety_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(plantVarietyService.remove(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PlantVarietyResponse.BIZ_CODE,
                PlantVarietyResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "植物新品种权登记导出", sheets = {@Sheet(sheetName = "植物新品种权列表")})
    @Operation(summary = "导出")
    //@PreAuthorize("@pms.hasPermission('plant_variety_export')")
    public void export(@RequestBody PlantVarietyExportWrapperRequest request) throws IOException {
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
        IPage<PlantVarietyResponse> pageData = plantVarietyService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "植物新品种权登记导出",
                // Sheet 名称
                "植物新品种权登记",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                PlantVarietyResponse.class
        );
    }
}
