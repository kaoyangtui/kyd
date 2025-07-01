package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.service.SyncDataService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "数据同步")
public class SyncDataController {

    private final SyncDataService syncDataService;

    @PostMapping("/sync/dept")
    @Operation(summary = "同步部门")
    public R<Boolean> syncDepts() {
        syncDataService.syncDepts();
        return R.ok();
    }

    @PostMapping("/sync/user")
    @Operation(summary = "同步用户")
    public R<Boolean> syncUsers() {
        syncDataService.syncUsers();
        return R.ok();
    }
}