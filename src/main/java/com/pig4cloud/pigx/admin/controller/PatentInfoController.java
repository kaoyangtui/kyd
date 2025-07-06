package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
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
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 专利信息表
 *
 * @author zl
 * @date 2025-04-15 13:09:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patent")
@Tag(description = "patent", name = "专利信息表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentInfoController {

    private final PatentInfoService patentInfoService;

    /**
     * ES专利分页检索
     */
    //@PostMapping("/es/page")
    //public R<IPage<PatentEsPageResponse>> esPage(@RequestBody PatentEsPageRequest request) {
    //    return R.ok(patentInfoService.esPage(request));
    //}

    @GetMapping("/page")
    @Operation(summary = "专利信息分页查询")
    public R<IPage<PatentInfoResponse>> page(@ParameterObject PageRequest pageRequest,
                                             @ParameterObject PatentPageRequest request) {
        return R.ok(patentInfoService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @Operation(summary = "全国专利查询")
    @PostMapping("/search")
    public R<IPage<PatentSearchListRes>> search(@RequestBody PatentSearchListReq req) {
        return R.ok(patentInfoService.searchList(req));
    }

    @Operation(summary = "专利认领")
    @PostMapping("/claim")
    public R<Boolean> claim(@RequestBody PatentClaimRequest req) {
        return R.ok();
    }

    @Operation(summary = "专利否认领")
    @PostMapping("/unclaim")
    public R<Boolean> unclaim() {
        return R.ok();
    }

    @PostMapping("/shelf")
    @Operation(summary = "专利上下架")
    public R<Boolean> shelf(@RequestBody PatentShelfRequest request) {
        return R.ok();
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentInfoResponse.BIZ_CODE,
                PatentInfoResponse.class
        );
        return R.ok(fields);
    }


    @PostMapping("/export")
    @ResponseExcel(name = "专利数据导出", sheets = {@Sheet(sheetName = "专利列表")})
    @Operation(summary = "专利数据导出")
    public void export(@RequestBody PatentExportWrapperRequest request) throws IOException {
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
        IPage<PatentInfoResponse> pageData = patentInfoService.pageResult(
                new Page<>(), request.getQuery()
        );

        // 4. 调用通用导出工具
        ExcelExportUtil.exportByBean(
                response,
                // 文件名（不带 .xlsx）
                "专利数据导出",
                // Sheet 名称
                "专利数据",
                // DTO 列表
                pageData.getRecords(),
                // 要导出的字段 keys
                request.getExport().getFieldKeys(),
                // DTO 类型
                PatentInfoResponse.class
        );
    }

    @GetMapping("/detail")
    @Operation(summary = "专利信息详情")
    public R<PatentDetailResponse> detail(@RequestParam String pid) {
        return R.ok(patentInfoService.getDetailByPid(pid));
    }
}