/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.sys.SysUserPageRequest;
import com.pig4cloud.pigx.admin.service.SystemService;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/system")
@Tag(description = "管理模块", name = "管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SystemController {

    private final SystemService systemService;

    @Operation(summary = "用户分页")
    @GetMapping("/user/page")
    public R<IPage<UserVO>> page(@ParameterObject PageRequest pageRequest,
                                  @ParameterObject SysUserPageRequest req) {
        IPage<UserVO> result = systemService.pageUserVO(PageUtil.toPage(pageRequest), req);
        return R.ok(result);
    }
}
