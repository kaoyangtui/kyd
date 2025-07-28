package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandPageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.expert.ExpertPageRequest;
import com.pig4cloud.pigx.admin.dto.expert.ExpertResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.dto.user.UserFollowCreateRequest;
import com.pig4cloud.pigx.admin.dto.user.UserFollowRemoveRequest;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoliang
 */
@RestController
@RequestMapping("/user/follow")
@Tag(name = "用户关注相关", description = "用户注册与密码管理")
@RequiredArgsConstructor
public class UserFollowController {

    private final UserFollowService userFollowService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final ExpertService expertService;


    @PostMapping("/create")
    @Operation(summary = "关注")
    public R<Boolean> follow(@RequestBody UserFollowCreateRequest request) {
        return R.ok(userFollowService.follow(SecurityUtils.getUser().getId(), request.getFollowType(), request.getFollowId()));
    }

    @PostMapping("/uncreate")
    @Operation(summary = "取消关注")
    public R<Boolean> unfollow(@RequestBody UserFollowRemoveRequest request) {
        return R.ok(userFollowService.unfollow(SecurityUtils.getUser().getId(), request.getFollowType(), request.getFollowId()));
    }

    @GetMapping("/check")
    @Operation(summary = "是否已关注")
    public R<Boolean> isFollowed(@RequestParam String followType, @RequestParam Long followId) {
        return R.ok(userFollowService.isFollowed(SecurityUtils.getUser().getId(), followType, followId));
    }

    @PostMapping("/patent/page")
    @Operation(summary = "我的关注-专利")
    public R<IPage<PatentInfoResponse>> resetPwdStep2(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(PatentInfoResponse.BIZ_CODE);
        PatentPageRequest request = new PatentPageRequest();
        request.setIds(followIds);
        return R.ok(patentInfoService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/result/page")
    @Operation(summary = "我的关注-成果")
    public R<IPage<ResultResponse>> followResultPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(ResultResponse.BIZ_CODE);
        ResultPageRequest request = new ResultPageRequest();
        request.setIds(followIds);
        return R.ok(resultService.pageResult(PageUtil.toPage(pageRequest), request));
    }


    @PostMapping("/demand/page")
    @Operation(summary = "我的关注-企业需求")
    public R<IPage<DemandResponse>> followDemandPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(DemandResponse.BIZ_CODE);
        DemandPageRequest request = new DemandPageRequest();
        request.setIds(followIds);
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }


    @PostMapping("/expert/page")
    @Operation(summary = "我的关注-专家")
    public R<IPage<ExpertResponse>> followExpertPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(ExpertResponse.BIZ_CODE);
        ExpertPageRequest request = new ExpertPageRequest();
        request.setIds(followIds);
        return R.ok(expertService.pageResult(PageUtil.toPage(pageRequest), request));
    }

}
