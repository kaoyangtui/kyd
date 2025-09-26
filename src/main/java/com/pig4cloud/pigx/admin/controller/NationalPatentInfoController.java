package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowCreateRequest;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowPageReq;
import com.pig4cloud.pigx.admin.dto.patent.PatentDetailResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchListReq;
import com.pig4cloud.pigx.admin.service.NationalPatentFollowService;
import com.pig4cloud.pigx.admin.service.NationalPatentInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 全国专利搜索信息表
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/nationalPatentInfo")
@Tag(description = "nationalPatentInfo", name = "全国专利相关")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
@Validated
public class NationalPatentInfoController {

    private final NationalPatentInfoService nationalPatentInfoService;
    private final NationalPatentFollowService nationalPatentFollowService;

    @Operation(summary = "全国专利查询")
    @PostMapping("/page")
    public R<IPage<PatentInfoResponse>> page(@RequestBody PatentSearchListReq req) {
        return R.ok(nationalPatentInfoService.searchList(req));
    }

    @GetMapping("/detail")
    @Operation(summary = "全国专利信息详情")
    public R<PatentDetailResponse> detail(@RequestParam String pid) {
        return R.ok(nationalPatentInfoService.getDetailByPid(pid));
    }

    @Operation(summary = "关注全国专利")
    @PostMapping("/follow")
    public R<Boolean> follow(@RequestBody @Validated NationalPatentFollowCreateRequest req) {
        Long userId = SecurityUtils.getUser().getId();
        // tenantId 有统一拦截器，这里不需要传；Service 内如需可自行获取
        boolean ok = nationalPatentFollowService.follow(userId, req);
        return R.ok(ok);
    }

    @Operation(summary = "取消关注全国专利")
    @PostMapping("/unfollow")
    public R<Boolean> unfollow(@RequestParam String pid) {
        Long userId = SecurityUtils.getUser().getId();
        boolean ok = nationalPatentFollowService.unfollow(userId, pid);
        return R.ok(ok);
    }

    @Operation(summary = "我关注的全国专利分页")
    @PostMapping("/follow/page")
    public R<IPage<PatentInfoResponse>> myFollowPage(@RequestBody @Validated NationalPatentFollowPageReq req) {
        Long userId = SecurityUtils.getUser().getId();
        return R.ok(nationalPatentInfoService.pageMyFollow(userId, req));
    }
}
