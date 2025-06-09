package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/page")
    @Operation(summary = "专利信息分页查询")
    public R<IPage<PatentInfoResponse>> page(@ParameterObject PageRequest pageRequest,
                                             @ParameterObject PatentPageRequest request) {
        return R.ok();
    }

    @GetMapping("/detail")
    @Operation(summary = "专利信息详情")
    public R<IPage<PatentDetailResponse>> detail(@RequestParam String pid) {
        return R.ok();
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

}