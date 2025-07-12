package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentInventorVO;
import com.pig4cloud.pigx.admin.dto.patent.PatentUnClaimRequest;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.admin.service.PatentInventorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专利发明人
 *
 * @author pigx
 * @date 2025-07-12 11:12:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patent/inventor" )
@Tag(description = "patentInventor" , name = "专利发明人管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentInventorController {

    private final  PatentInventorService patentInventorService;


    @GetMapping("/list")
    @Operation(summary = "查询专利发明人列表")
    public R<List<PatentInventorVO>> getPatentInventors(@RequestParam("pid") String pid) {
        if (StrUtil.isBlank(pid)) {
            return R.failed("专利PID不能为空");
        }
        List<PatentInventorVO> list = patentInventorService.listByPid(pid);
        return R.ok(list);
    }



    @Operation(summary = "专利认领")
    @PostMapping("/claim")
    public R<Boolean> claim(@RequestBody PatentClaimRequest req) {
        return R.ok(patentInventorService.claim(req));
    }

    @Operation(summary = "专利否认领")
    @PostMapping("/unclaim")
    public R<Boolean> unclaim(@RequestBody PatentUnClaimRequest req) {
        return R.ok(patentInventorService.unClaim(req));
    }
}