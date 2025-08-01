package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.service.MatchService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoliang
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@Tag(name = "供需匹配管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/demand")
    @Operation(summary = "企业需求匹配")
    //@PreAuthorize("@pms.hasPermission('demand_add')")
    public R<Boolean> demandMatch() {
        return R.ok(matchService.demandMatch());
    }

}