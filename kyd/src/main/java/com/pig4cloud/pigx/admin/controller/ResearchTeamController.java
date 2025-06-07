package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.*;
import com.pig4cloud.pigx.admin.service.ResearchTeamService;
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
@RequestMapping("/researchTeam")
@Tag(name = "科研团队管理", description = "科研团队管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ResearchTeamController {

    private final ResearchTeamService researchTeamService;

    @GetMapping("/page")
    @Operation(summary = "科研团队-分页查询")
    public R<IPage<ResearchTeamResponse>> page(@ParameterObject PageRequest pageRequest,
                                               @ParameterObject ResearchTeamPageRequest request) {
        return R.ok(researchTeamService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<ResearchTeamResponse> detail(@RequestBody IdRequest request) {
        return R.ok(researchTeamService.getDetail(request.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "新增")
    @SysLog("新增科研团队")
    public R<Boolean> create(@RequestBody ResearchTeamCreateRequest request) {
        return R.ok(researchTeamService.create(request));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @SysLog("修改科研团队")
    public R<Boolean> update(@RequestBody ResearchTeamUpdateRequest request) {
        return R.ok(researchTeamService.update(request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除科研团队")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(researchTeamService.remove(request.getIds()));
    }

    @PostMapping("/shelf")
    @Operation(summary = "上下架")
    @SysLog("科研团队上下架")
    public R<Boolean> shelf(@RequestBody ResearchTeamShelfRequest request) {
        return R.ok(researchTeamService.shelfByIds(request.getIds(), request.getShelfStatus()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                ResearchTeamResponse.BIZ_CODE,
                ResearchTeamResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody ResearchTeamExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }

        IPage<ResearchTeamResponse> pageData = researchTeamService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "科研团队导出",
                "科研团队列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                ResearchTeamResponse.class
        );
    }
}
