package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.toolkit.StrUtils;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.service.DimEcService;
import com.pig4cloud.pigx.admin.service.DimMajorService;
import com.pig4cloud.pigx.admin.service.DimRegionService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author zhaoliang
 */
@Tag(name = "维度信息管理")
@RestController
@RequestMapping("/dim")
@RequiredArgsConstructor
public class DimController {
    private final DimEcService dimEcService;
    private final DimRegionService dimRegionService;
    private final SysUserService sysUserService;
    private final DimMajorService dimMajorService;

    @Operation(summary = "所属领域")
    @PostMapping("/ec/tree")
    public R<List<Tree<String>>> ecTree() {
        return R.ok(dimEcService.tree());
    }

    @Operation(summary = "所属地区")
    @PostMapping("/region/tree")
    public R<List<Tree<Integer>>> regionTree() {
        return R.ok(dimRegionService.tree());
    }

    @Operation(summary = "学科")
    @PostMapping("/major/tree")
    public R<List<Tree<Integer>>> majorTree() {
        return R.ok(dimMajorService.tree());
    }

    /**
     * 获取用户列表
     *
     * @param key 用户名或工号
     * @return 用户列表
     */
    @GetMapping("/getUserList")
    public R<List<SysUser>> getUserListByUserNameOrCode(String key) {
        // Check if the key is empty or null
        if (StrUtils.isBlank(key)) {
            return R.ok(Collections.emptyList());
        }

        // Query users by username or code (assuming code refers to "工号")
        List<SysUser> userList = sysUserService.list(Wrappers.<SysUser>lambdaQuery()
                .like(StrUtil.isNotBlank(key), SysUser::getUsername, key)
                .or()
                .like(StrUtil.isNotBlank(key), SysUser::getCode, key)
        );
        return R.ok(userList);
    }

}
