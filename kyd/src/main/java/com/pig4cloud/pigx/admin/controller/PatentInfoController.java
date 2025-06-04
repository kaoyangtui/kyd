package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchResponse;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.admin.model.request.PatentSearchListReq;
import com.pig4cloud.pigx.admin.model.response.PatentSearchListRes;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
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

    private final PatentInfoService patentInfoService;

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


}