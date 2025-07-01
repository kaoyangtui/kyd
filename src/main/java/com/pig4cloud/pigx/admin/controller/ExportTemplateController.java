package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportTemplateCreateRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportTemplatePageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportTemplateResponse;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportTemplateUpdateRequest;
import com.pig4cloud.pigx.admin.service.ExportTemplateService;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 用户导出字段模板配置表
 *
 * @author pigx
 * @date 2025-05-12 09:07:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/exportTemplate")
@Tag(description = "exportTemplate", name = "用户导出字段模板配置表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExportTemplateController {

    private final ExportTemplateService exportTemplateService;


    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<ExportTemplateResponse>> page(@ParameterObject PageRequest pageRequest,
                                                 @ParameterObject ExportTemplatePageRequest request) {
        return R.ok(exportTemplateService.pageTemplate(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ExportTemplateResponse> detail(@RequestBody IdRequest request) {
        return R.ok(exportTemplateService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增导出模板")
    public R<Boolean> create(@RequestBody @Valid ExportTemplateCreateRequest request) {
        return R.ok(exportTemplateService.createTemplate(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改导出模板")
    public R<Boolean> update(@RequestBody ExportTemplateUpdateRequest request) {
        return R.ok(exportTemplateService.updateTemplate(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除导出模板")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(exportTemplateService.removeTemplates(request.getIds()));
    }

    @PostMapping("/set-default")
    @Operation(summary = "设置为默认")
    @SysLog("设置模板为默认")
    public R<Boolean> setDefault(@RequestBody IdRequest request) {
        return R.ok(exportTemplateService.setDefault(request.getId()));
    }
}