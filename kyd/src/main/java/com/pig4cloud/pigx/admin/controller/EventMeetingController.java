package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.EventMeetingApplyService;
import com.pig4cloud.pigx.admin.service.EventMeetingService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventMeeting")
@Tag(name = "活动会议管理", description = "活动会议管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class EventMeetingController {

    private final EventMeetingService eventMeetingService;
    private final EventMeetingApplyService eventMeetingApplyService;

    // 主表分页
    @GetMapping("/page")
    @Operation(summary = "活动会议分页查询")
    public R<IPage<EventMeetingResponse>> page(@ParameterObject PageRequest pageRequest, @ParameterObject EventMeetingPageRequest request) {
        return R.ok(eventMeetingService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    // 主表详情
    @PostMapping("/detail")
    @Operation(summary = "活动会议详情")
    public R<EventMeetingResponse> detail(@RequestBody IdRequest request) {
        return R.ok(eventMeetingService.getDetail(request.getId()));
    }

    // 新增
    @PostMapping("/create")
    @Operation(summary = "新增活动会议")
    @SysLog("新增活动会议")
    public R<Boolean> create(@RequestBody EventMeetingCreateRequest request) {
        return R.ok(eventMeetingService.createMeeting(request));
    }

    // 修改
    @PostMapping("/update")
    @Operation(summary = "修改活动会议")
    @SysLog("修改活动会议")
    public R<Boolean> update(@RequestBody EventMeetingUpdateRequest request) {
        return R.ok(eventMeetingService.updateMeeting(request));
    }

    // 删除
    @PostMapping("/remove")
    @Operation(summary = "删除活动会议")
    @SysLog("删除活动会议")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(eventMeetingService.removeMeetings(request.getIds()));
    }

    // 导出字段
    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                EventMeetingResponse.BIZ_CODE,
                EventMeetingResponse.class
        );
        return R.ok(fields);
    }

    // 导出
    @PostMapping("/export")
    @Operation(summary = "导出活动会议列表")
    public void export(@RequestBody EventMeetingExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<EventMeetingResponse> pageData = eventMeetingService.pageResult(
                new Page<>(), request.getQuery()
        );
        ExcelExportUtil.exportByBean(
                response,
                "活动会议导出",
                "活动会议列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                EventMeetingResponse.class
        );
    }

    // --- 子表：活动报名 ---

    // 报名分页列表
    @GetMapping("/apply/page")
    @Operation(summary = "活动报名信息分页查询")
    public R<IPage<EventMeetingApplyResponse>> applyPage(@ParameterObject PageRequest pageRequest, @ParameterObject EventMeetingApplyPageRequest request) {
        return R.ok(eventMeetingApplyService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    // 报名信息详情
    @PostMapping("/apply/detail")
    @Operation(summary = "活动报名信息详情")
    public R<EventMeetingApplyResponse> applyDetail(@RequestBody IdRequest request) {
        return R.ok(eventMeetingApplyService.getDetail(request.getId()));
    }

    // 新增报名
    @PostMapping("/apply/create")
    @Operation(summary = "新增活动报名信息")
    @SysLog("新增活动报名信息")
    public R<Boolean> applyCreate(@RequestBody EventMeetingApplyCreateRequest request) {
        return R.ok(eventMeetingApplyService.createApply(request));
    }

    // 删除报名
    @PostMapping("/apply/remove")
    @Operation(summary = "删除活动报名信息")
    @SysLog("删除活动报名信息")
    public R<Boolean> applyRemove(@RequestBody IdListRequest request) {
        return R.ok(eventMeetingApplyService.removeApplies(request.getIds()));
    }
}
