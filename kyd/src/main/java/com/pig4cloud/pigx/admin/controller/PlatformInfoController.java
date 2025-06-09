package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.*;
import com.pig4cloud.pigx.admin.service.PlatformInfoService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/platformInfo")
@Tag(name = "关于平台内容管理", description = "关于平台内容管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PlatformInfoController {

    private final PlatformInfoService platformInfoService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<PlatformInfoResponse>> page(@ParameterObject Page page, @ParameterObject PlatformInfoPageRequest request) {
        return R.ok(platformInfoService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<PlatformInfoResponse> detail(@RequestBody IdRequest request) {
        return R.ok(platformInfoService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增关于平台内容")
    public R<Boolean> create(@RequestBody @Valid PlatformInfoCreateRequest request) {
        return R.ok(platformInfoService.createInfo(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改关于平台内容")
    public R<Boolean> update(@RequestBody PlatformInfoUpdateRequest request) {
        return R.ok(platformInfoService.updateInfo(request));
    }
}
