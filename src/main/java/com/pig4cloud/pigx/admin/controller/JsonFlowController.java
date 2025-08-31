package com.pig4cloud.pigx.admin.controller;

import com.google.common.collect.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jf")
@Tag(name = "流程引擎")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class JsonFlowController {

    @PostMapping("/node/approver")
    @Operation(summary = "获取流程节点审批人")
    public R<List<String>> nodeApprover(@RequestBody Map<String, Object> request) {
        log.info("$$$$$$$$$$$$$$$获取流程节点审批人,{}", request);
        return R.ok(Lists.newArrayList("USER_1"));
    }
}