package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.entity.SysUserRole;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysRoleService;
import com.pig4cloud.pigx.admin.service.SysUserRoleService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jf")
@Tag(name = "流程引擎")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class JFController {

    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleService sysRoleService;
    private final SysDeptService sysDeptService;

    @PostMapping("/node/approver")
    @Operation(summary = "获取流程节点审批人")
    public R<List<Long>> nodeApprover(@RequestBody Map<String, Object> request) {
        log.info("$$$$$$$$$$$$$$$获取流程节点审批人,{}", request);
        String roleCode = MapUtil.getStr(request, "roleCode");
        Long userId = MapUtil.getLong(request, "userId");
        Long deptId = MapUtil.getLong(request, "deptId");
        return R.ok(getApproverUserIds(roleCode, userId, deptId));
    }


    public List<Long> getApproverUserIds(String roleCode, Long createUserId, Long createDeptId) {
        SysRole sysRole = sysRoleService.lambdaQuery().eq(SysRole::getRoleCode, roleCode).one();
        //获取所有拥有角色组的用户 id
        List<Long> list = sysUserRoleService
                .list(Wrappers.<SysUserRole>lambdaQuery()
                        .eq(SysUserRole::getRoleId, sysRole.getRoleId()))
                .stream()
                .map(SysUserRole::getUserId).collect(Collectors.toList());
        List<SysUser> sysUserList = sysUserService.listByIds(list);
        List<Long> results = Lists.newArrayList();
        for (SysUser sysUser : sysUserList) {
            if (this.checkDataScope(sysUser, createUserId, createDeptId)) {
                results.add(sysUser.getUserId());
            }
        }
        return results;
    }

    /**
     * 校验用户是否拥有工单人的数据查看权限
     *
     * @param sysUser
     * @param createUserId
     * @param createDeptId
     */
    private Boolean checkDataScope(SysUser sysUser, Long createUserId, Long createDeptId) {
        //获取用户角色组
        List<Long> roleIdList = sysUserRoleService
                .list(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getUserId())).stream()
                .map(SysUserRole::getRoleId).collect(Collectors.toList());
        //根据角色组获取用户最大数据权限
        SysRole role = sysRoleService.listByIds(roleIdList)
                .stream()
                .min(Comparator.comparingInt(SysRole::getDsType)).orElse(null);
        //角色有可能已经删除了
        if (role == null) {
            return false;
        }
        //用户最大数据权限
        Integer dsType = role.getDsType();
        //定义拥有的部门权限组
        List<Long> deptList = org.apache.commons.compress.utils.Lists.newArrayList();
        // 查询全部
        if (DataScopeTypeEnum.ALL.getType() == dsType) {
            return true;
        } else if (DataScopeTypeEnum.CUSTOM.getType() == dsType && StrUtil.isNotBlank(role.getDsScope())) {
            // 自定义
            String dsScope = role.getDsScope();
            deptList.addAll(Arrays.stream(dsScope.split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList()));
        } else if (DataScopeTypeEnum.OWN_CHILD_LEVEL.getType() == dsType) {
            // 查询本级及其下级
            //todo 获取部门及子部门
            //deptList.addAll(deptIdList);
        } else if (DataScopeTypeEnum.OWN_LEVEL.getType() == dsType) {
            // 只查询本级
            deptList.add(sysUser.getDeptId());
        } else if (DataScopeTypeEnum.SELF_LEVEL.getType() == dsType) {
            // 只查询本人
            if (sysUser.getUserId().equals(createUserId)) {
                return true;
            }
        } else {
            log.error(StrUtil.format("【数据权限类型错误】用户id:{},用户部门id:{},工单人id:{},工单人部门id:{}", sysUser.getUserId(), sysUser.getDeptId(), createUserId, createDeptId));
            return false;
        }
        if (deptList.contains(createDeptId)) {
            return true;
        }
        return false;
    }
}