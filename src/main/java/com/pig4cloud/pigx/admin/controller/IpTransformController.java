package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.ipTransform.*;
import com.pig4cloud.pigx.admin.service.IpTransformService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/ipTransform")
@Tag(name = "知识产权转化管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class IpTransformController {

    private final IpTransformService ipTransformService;

    @PostMapping("/create")
    @Operation(summary = "新增转化")
    //@PreAuthorize("@pms.hasPermission('ip_transform_add')")
    public R<Boolean> create(@RequestBody @Valid IpTransformCreateRequest request) {
        return R.ok(ipTransformService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新转化")
    //@PreAuthorize("@pms.hasPermission('ip_transform_edit')")
    public R<Boolean> update(@RequestBody IpTransformUpdateRequest request) {
        return R.ok(ipTransformService.update(request));
    }

    @PostMapping("/detail")
    @Operation(summary = "查看详情")
    //@PreAuthorize("@pms.hasPermission('ip_transform_view')")
    public R<IpTransformResponse> detail(@RequestBody IdRequest request) {
        return R.ok(ipTransformService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "批量删除")
    //@PreAuthorize("@pms.hasPermission('ip_transform_del')")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(ipTransformService.removeByIds(request.getIds()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    //@PreAuthorize("@pms.hasPermission('ip_transform_view')")
    public R<IPage<IpTransformResponse>> page(@ParameterObject PageRequest pageRequest,
                                              @ParameterObject IpTransformPageRequest request) {
        return R.ok(ipTransformService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    //@PreAuthorize("@pms.hasPermission('ip_transform_export')")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse response = ExportFieldHelper.buildExportFieldList(
                IpTransformResponse.BIZ_CODE,
                IpTransformResponse.class
        );
        return R.ok(response);
    }

    @PostMapping("/export")
    @Operation(summary = "知识产权转化导出")
    public void export(@RequestBody IpTransformExportWrapperRequest request) throws IOException {
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
        IPage<IpTransformResponse> pageData = ipTransformService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "知识产权转化导出",
                // Sheet 名称
                "知识产权转化",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                IpTransformResponse.class
        );
    }

    @PostMapping("/confirmPayment")
    @Operation(summary = "到款确认（将到款状态置为已到款）")
//@PreAuthorize("@pms.hasPermission('ip_transform_pay_confirm')")
    public R<Boolean> confirmPayment(@RequestBody IdRequest request) {
        return R.ok(ipTransformService.confirmPayment(request.getId()));
    }

}
