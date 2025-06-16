package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.consult.ConsultCreateRequest;
import com.pig4cloud.pigx.admin.dto.consult.ConsultPageRequest;
import com.pig4cloud.pigx.admin.dto.consult.ConsultResponse;
import com.pig4cloud.pigx.admin.dto.demand.DemandCreateRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandPageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.expert.ExpertPageRequest;
import com.pig4cloud.pigx.admin.dto.expert.ExpertResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultPageRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.dto.user.ResetPwdStep1Request;
import com.pig4cloud.pigx.admin.dto.user.ResetPwdStep2Request;
import com.pig4cloud.pigx.admin.dto.user.UserProfileUpdateRequest;
import com.pig4cloud.pigx.admin.dto.user.UserRegisterRequest;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoliang
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关", description = "用户注册与密码管理")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFollowService userFollowService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final ExpertService expertService;
    private final ConsultService consultService;
    private final EventMeetingService eventMeetingService;
    private final EventMeetingApplyService eventMeetingApplyService;

    @PostMapping("/profile/update")
    @Operation(summary = "修改个人信息")
    public R<Boolean> updateProfile(@RequestBody @Valid UserProfileUpdateRequest request) {
        userService.updateProfile(request);
        return R.ok(Boolean.TRUE);
    }

    @PostMapping("/sendCode")
    @Operation(summary = "发送验证码")
    public R<Boolean> sendCode(@RequestParam String contactInfo) {
        userService.sendCode(contactInfo);
        return R.ok(Boolean.TRUE);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<Boolean> register(@RequestBody @Valid UserRegisterRequest request) {
        return R.ok(userService.register(request));
    }

    @PostMapping("/resetPwd/step1")
    @Operation(summary = "重置密码-校验验证码")
    public R<Boolean> resetPwdStep1(@RequestBody @Valid ResetPwdStep1Request request) {
        return R.ok(userService.resetPwdStep1(request));
    }

    @PostMapping("/resetPwd/step2")
    @Operation(summary = "重置密码-设置新密码")
    public R<Boolean> resetPwdStep2(@RequestBody @Valid ResetPwdStep2Request request) {
        return R.ok(userService.resetPwdStep2(request));
    }

    @PostMapping("/userFollow/patent/page")
    @Operation(summary = "我的关注-专利")
    public R<IPage<PatentInfoResponse>> resetPwdStep2(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(PatentInfoResponse.BIZ_CODE);
        PatentPageRequest request = new PatentPageRequest();
        request.setIds(followIds);
        return R.ok(patentInfoService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/userFollow/result/page")
    @Operation(summary = "我的关注-成果")
    public R<IPage<ResultResponse>> followResultPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(ResultResponse.BIZ_CODE);
        ResultPageRequest request = new ResultPageRequest();
        request.setIds(followIds);
        return R.ok(resultService.pageResult(PageUtil.toPage(pageRequest), request));
    }


    @PostMapping("/follow/demand/page")
    @Operation(summary = "我的关注-企业需求")
    public R<IPage<DemandResponse>> followDemandPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(DemandResponse.BIZ_CODE);
        DemandPageRequest request = new DemandPageRequest();
        request.setIds(followIds);
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }


    @PostMapping("/follow/expert/page")
    @Operation(summary = "我的关注-专家")
    public R<IPage<ExpertResponse>> followExpertPage(@ParameterObject PageRequest pageRequest) {
        List<Long> followIds = userFollowService.getFollowIds(ExpertResponse.BIZ_CODE);
        ExpertPageRequest request = new ExpertPageRequest();
        request.setIds(followIds);
        return R.ok(expertService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/demand/page")
    @Operation(summary = "我的需求")
    public R<IPage<DemandResponse>> demandPage(@ParameterObject PageRequest pageRequest) {
        DemandPageRequest request = new DemandPageRequest();
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/demand/create")
    @Operation(summary = "新增需求")
    public R<Boolean> demandCreate(@RequestBody @Valid DemandCreateRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(demandService.create(request));
    }

    @GetMapping("/consult/page")
    @Operation(summary = "我的咨询")
    public R<IPage<ConsultResponse>> consultPage(@ParameterObject PageRequest pageRequest) {
        ConsultPageRequest request = new ConsultPageRequest();
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(consultService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/consult/create")
    @Operation(summary = "新增咨询")
    public R<Boolean> consultCreate(@RequestBody @Valid ConsultCreateRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(consultService.create(request));
    }

    @PostMapping("/eventMeeting/apply/create")
    @Operation(summary = "活动会议报名")
    public R<Boolean> applyCreate(@RequestBody @Valid EventMeetingApplyCreateRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(eventMeetingApplyService.createApply(request));
    }

    @GetMapping("/eventMeeting/page")
    @Operation(summary = "活动会议我的报名")
    public R<IPage<EventMeetingResponse>> page(@ParameterObject PageRequest pageRequest) {
        EventMeetingPageRequest request = new EventMeetingPageRequest();
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(eventMeetingService.pageResult(PageUtil.toPage(pageRequest), request));
    }

}
