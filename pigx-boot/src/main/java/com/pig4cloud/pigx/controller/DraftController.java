package com.pig4cloud.pigx.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.servicebiz.DraftServiceBiz;
import com.pig4cloud.pigx.vo.DraftQueryVO;
import com.pig4cloud.pigx.vo.DraftSaveVO;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 草稿管理
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/draft")
@Tag(description = "draft", name = "草稿管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DraftController {


    private final DraftServiceBiz draftServiceBiz;

    /**
     * 保存草稿
     *
     * @param request 草稿数据
     * @return 保存结果
     */
    @SysLog("保存草稿")
    @PostMapping("/save")
    @HasPermission("draft_save")
    public R saveDraft(@Valid @RequestBody DraftSaveVO request) {
        return R.ok(draftServiceBiz.saveOrUpdateDraft(request));
    }

    /**
     * 提交草稿
     *
     * @param request 草稿提交请求
     * @return 提交结果
     */
    @SysLog("提交草稿")
    @PutMapping("/submit")
    @HasPermission("draft_submit")
    public R submitDraft(@Valid @RequestBody DraftSaveVO request) {
        return R.ok(draftServiceBiz.submitDraft(request));
    }

    /**
     * 查询草稿
     *
     * @param queryVO 查询参数
     * @return 草稿信息
     */
    @GetMapping("/query")
    @HasPermission("draft_query")
    public R getDraft(@Valid DraftQueryVO queryVO) {
        return R.ok(draftServiceBiz.getDraft(queryVO));
    }
}