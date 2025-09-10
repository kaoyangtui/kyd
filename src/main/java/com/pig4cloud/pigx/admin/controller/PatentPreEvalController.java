package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.PatentPreEval.*;
import com.pig4cloud.pigx.admin.service.PatentPreEvalService;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/patentPreEval")
@Tag(name = "专利申请前评估")
public class PatentPreEvalController {

    private final PatentPreEvalService patentPreEvalService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<Page<PatentPreEvalResponse>> page(@ParameterObject PatentPreEvalPageRequest req, @ParameterObject PageRequest pageRequest) {
        Page<?> page = PageUtil.toPage(pageRequest);
        Page<PatentPreEvalResponse> result = patentPreEvalService.pageResult(page, req);
        return R.ok(result);
    }

    @GetMapping("/detail")
    @Operation(summary = "详情")
    public R<PatentPreEvalResponse> detail(@ParameterObject PatentPreEvalDetailRequest request) {
        PatentPreEvalResponse resp = patentPreEvalService.detail(request);
        return R.ok(resp);
    }

    @PostMapping("/create")
    @Operation(summary = "提交")
    public R<Boolean> create(@RequestBody PatentPreEvalCreateRequest request) {
        boolean ok = patentPreEvalService.create(request);
        return R.ok(ok);
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    public R<Boolean> update(@RequestBody PatentPreEvalUpdateRequest request) {
        boolean ok = patentPreEvalService.update(request);
        return R.ok(ok);
    }

    @PostMapping("/update/result")
    @Operation(summary = "修改结果信息")
    public R<Boolean> updateResult(@RequestBody PatentPreEvalUpdateResultRequest request) {
        boolean ok = patentPreEvalService.updateResult(request);
        return R.ok(ok);
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    public R<Boolean> remove(@RequestBody PatentPreEvalRemoveRequest request) {
        boolean ok = patentPreEvalService.remove(request.getIds());
        return R.ok(ok);
    }
}
