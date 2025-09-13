package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
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
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.user.*;
import com.pig4cloud.pigx.admin.entity.UserEntity;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @author zhaoliang
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关", description = "用户注册与密码管理")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DemandService demandService;
    private final ConsultService consultService;
    private final EventMeetingService eventMeetingService;
    private final EventMeetingApplyService eventMeetingApplyService;

    @GetMapping("/customer/page")
    @Operation(summary = "分页查询user")
    public R<IPage<UserResponse>> page(@ParameterObject PageRequest pageRequest, @ParameterObject UserPageRequest request) {
        return R.ok(userService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/customer/remove")
    @Operation(summary = "删除user")
    @SysLog("删除user")
    public R<Boolean> remove(@RequestBody @Valid IdListRequest request) {
        return R.ok(userService.removeUser(request));
    }

    @PostMapping("/customer/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                UserResponse.BIZ_CODE,
                UserResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/customer/export")
    @ResponseExcel(name = "用户导出", sheets = {@Sheet(sheetName = "用户列表")})
    @Operation(summary = "导出用户")
    public void export(@RequestBody @Valid UserExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<UserResponse> pageData = userService.pageResult(
                new Page<>(), request.getQuery()
        );

        ExcelExportUtil.exportByBean(
                response,
                "用户导出",
                "用户列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                UserResponse.class
        );
    }

    @GetMapping("/profile/info")
    @Operation(summary = "个人信息")
    public R<UserProfileRequest> getProfile() {
        UserEntity user = userService.getById(SecurityUtils.getUser().getId());
        return R.ok(BeanUtil.toBean(user, UserProfileRequest.class));
    }

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

    @PostMapping("/resetPwd/login")
    @Operation(summary = "重置密码-登录状态")
    public R<Boolean> updatePassword(@RequestBody UpdatePasswordRequest request) {
        return R.ok(userService.updatePassword(request));
    }


    @PostMapping("/demand/page")
    @Operation(summary = "我的需求")
    public R<IPage<DemandResponse>> demandPage(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject DemandPageRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(demandService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/demand/create")
    @Operation(summary = "新增需求")
    public R<Boolean> demandCreate(@RequestBody @Valid DemandCreateRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        request.setCategory(1);
        return R.ok(demandService.create(request));
    }

    @GetMapping("/consult/page")
    @Operation(summary = "我的咨询")
    public R<IPage<ConsultResponse>> consultPage(@ParameterObject PageRequest pageRequest,
                                                 @ParameterObject ConsultPageRequest request) {
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
    public R<IPage<EventMeetingResponse>> page(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject EventMeetingPageRequest request) {
        request.setUserId(SecurityUtils.getUser().getId());
        return R.ok(eventMeetingService.pageResult(PageUtil.toPage(pageRequest), request));
    }

}
