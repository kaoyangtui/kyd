package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoCreateRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoPageRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoUpdateRequest;
import com.pig4cloud.pigx.admin.service.PlatformInfoService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/platformInfo")
@Tag(name = "关于平台内容管理", description = "关于平台内容管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PlatformInfoController {

    private final PlatformInfoService platformInfoService;

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<PlatformInfoResponse> detail() {
        return R.ok(platformInfoService.getDetail());
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改关于平台内容")
    public R<Boolean> update(@RequestBody PlatformInfoUpdateRequest request) {
        return R.ok(platformInfoService.updateInfo(request));
    }
}
