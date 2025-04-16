package com.pig4cloud.pigx.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.entity.PatentInfoEntity;
import com.pig4cloud.pigx.service.CniprService;
import com.pig4cloud.pigx.service.PatentInfoService;
import com.pig4cloud.pigx.service.PatentLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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


    @GetMapping("/test")
    public R test() {
        return R.ok(patentLogService.updateStatus());
    }

}