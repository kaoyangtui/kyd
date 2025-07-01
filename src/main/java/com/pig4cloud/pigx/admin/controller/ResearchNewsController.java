package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.*;
import com.pig4cloud.pigx.admin.service.ResearchNewsService;
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
 * 科研动态管理
 *
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/researchNews")
@Tag(name = "科研动态管理", description = "科研动态管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchNewsController {

    private final ResearchNewsService researchNewsService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ResearchNewsResponse>> page(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject ResearchNewsPageRequest request) {
        return R.ok(researchNewsService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ResearchNewsResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchNewsService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增科研动态")
    public R<Boolean> create(@RequestBody @Valid ResearchNewsCreateRequest request) {
        return R.ok(researchNewsService.createNews(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改科研动态")
    public R<Boolean> update(@RequestBody ResearchNewsUpdateRequest request) {
        return R.ok(researchNewsService.updateNews(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除科研动态")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchNewsService.removeNews(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResearchNewsResponse.BIZ_CODE,
                ResearchNewsResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ResearchNewsExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<ResearchNewsResponse> pageData = researchNewsService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "科研动态导出",
                "科研动态列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ResearchNewsResponse.class
        );
    }
}
