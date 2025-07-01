package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.assetPolicy.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.AssetPolicyService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assetPolicy")
@Tag(name = "资产政策管理", description = "资产政策管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AssetPolicyController {

    private final AssetPolicyService assetPolicyService;

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<AssetPolicyResponse>> page(@ParameterObject PageRequest pageRequest,
                                              @ParameterObject AssetPolicyPageRequest request) {
        return R.ok(assetPolicyService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<AssetPolicyResponse> detail(@RequestBody IdRequest request) {
        return R.ok(assetPolicyService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增资产政策")
    public R<Boolean> create(@RequestBody @Valid AssetPolicyCreateRequest request) {
        return R.ok(assetPolicyService.createPolicy(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改资产政策")
    public R<Boolean> update(@RequestBody AssetPolicyUpdateRequest request) {
        return R.ok(assetPolicyService.updatePolicy(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除资产政策")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(assetPolicyService.removePolicies(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                AssetPolicyResponse.BIZ_CODE,
                AssetPolicyResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody AssetPolicyExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<AssetPolicyResponse> pageData = assetPolicyService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "资产政策导出",
                "资产政策列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                AssetPolicyResponse.class
        );
    }
}
