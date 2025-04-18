package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.service.KunyidaPatentService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.admin.model.request.PatentSearchListReq;
import com.pig4cloud.pigx.admin.model.response.PatentSearchListRes;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.service.PatentLogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 专利信息表
 *
 * @author zl
 * @date 2025-04-15 13:09:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patentInfo")
@Tag(description = "patentInfo", name = "专利信息表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentInfoController {

    private final PatentLogService patentLogService;
    private final PatentInfoService patentInfoService;
    private final KunyidaPatentService kunyidaPatentService;

    /**
     * 搜索列表
     *
     * @param req 请求
     * @return R
     */
    @Operation(summary = "搜索列表")
    @PostMapping("/search_list")
    public R<IPage<PatentSearchListRes>> searchList(@RequestBody PatentSearchListReq req) {
        return R.ok(patentInfoService.searchList(req));
    }

    @GetMapping("/test")
    public R test() {
        return R.ok(patentLogService.updateStatus());
    }


    @GetMapping("/fetchAllPatents")
    public R fetchAllPatents() {
        kunyidaPatentService.fetchAllPatents();
        return R.ok();
    }

}