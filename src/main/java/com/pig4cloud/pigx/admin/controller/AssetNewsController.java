package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.AssetNewsService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/assetNews")
@Tag(name = "资产资讯管理", description = "资产资讯管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AssetNewsController {

    private final AssetNewsService assetNewsService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<AssetNewsResponse>> page(@ParameterObject PageRequest pageRequest,
                                            @ParameterObject AssetNewsPageRequest request) {
        return R.ok(assetNewsService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<AssetNewsResponse> detail(@RequestBody IdRequest request) {
        return R.ok(assetNewsService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增资产资讯")
    public R<Boolean> create(@RequestBody @Valid AssetNewsCreateRequest request) {
        return R.ok(assetNewsService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改资产资讯")
    public R<Boolean> update(@RequestBody AssetNewsUpdateRequest request) {
        return R.ok(assetNewsService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除资产资讯")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(assetNewsService.remove(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                AssetNewsResponse.BIZ_CODE,
                AssetNewsResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody AssetNewsExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<AssetNewsResponse> pageData = assetNewsService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "资产资讯导出",
                "资产资讯列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                AssetNewsResponse.class
        );
    }
}
