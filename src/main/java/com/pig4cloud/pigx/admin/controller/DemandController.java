package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demand.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.DemandReceiveService;
import com.pig4cloud.pigx.admin.service.DemandService;
import com.pig4cloud.pigx.admin.service.DemandSignupService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
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

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/demand")
@Tag(name = "企业需求管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DemandController {

    private final DemandService demandService;
    private final DemandSignupService demandSignupService;
    private final DemandReceiveService demandReceiveService;

    @PostMapping("/create")
    @Operation(summary = "新增企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_add')")
    public R<Boolean> create(@RequestBody @Valid DemandCreateRequest request) {
        return R.ok(demandService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_edit')")
    public R<Boolean> update(@RequestBody DemandUpdateRequest request) {
        return R.ok(demandService.update(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_view')")
    public R<IPage<DemandResponse>> page(@ParameterObject PageRequest pageRequest,
                                         @ParameterObject DemandPageRequest request) {
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_edit')")
    public R<Boolean> shelf(@RequestBody DemandShelfRequest request) {
        return R.ok(demandService.updateShelfStatus(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "企业需求详情")
    //@PreAuthorize("@pms.hasPermission('demand_view')")
    public R<DemandResponse> detail(@RequestBody IdRequest request) {
        return R.ok(demandService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(demandService.removeByIds(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(DemandResponse.BIZ_CODE);
        response.setFields(ExportFieldHelper.getFieldsFromDto(DemandResponse.class));
        return R.ok(response);
    }

    @PostMapping("/export")
    @ResponseExcel(name = "企业需求导出", sheets = {@Sheet(sheetName = "需求列表")})
    @Operation(summary = "导出企业需求")
    //@PreAuthorize("@pms.hasPermission('demand_export')")
    public void export(@RequestBody DemandExportWrapperRequest request) throws IOException {
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
        IPage<DemandResponse> pageData = demandService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "企业需求",
                // Sheet 名称
                "企业需求",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                DemandResponse.class
        );
    }

    @PostMapping("/signup")
    @Operation(summary = "提交企业需求报名")
    public R<Boolean> signup(@RequestBody DemandSignupRequest request) {
        return R.ok(demandSignupService.signup(request));
    }

    @PostMapping("/receive")
    @Operation(summary = "企业推送")
    public R<Boolean> receive(@RequestBody DemandReceiveRequest request) {
        return R.ok(demandReceiveService.receive(request));
    }

    @GetMapping("/receive/page")
    @Operation(summary = "分页查询需求接收信息")
    public R<IPage<DemandReceiveResponse>> receivePage(@ParameterObject PageRequest pageRequest,
                                                       @ParameterObject DemandReceivePageRequest request) {
        return R.ok(demandReceiveService.pageResult(PageUtil.toPage(pageRequest), request));
    }


}