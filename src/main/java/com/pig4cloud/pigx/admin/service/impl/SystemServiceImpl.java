package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.entity.SysUserRole;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.dto.sys.SysUserPageRequest;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemServiceImpl implements SystemService {

    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleService sysRoleService;

    @Override
    public IPage<UserVO> pageUserVO(Page page, SysUserPageRequest req) {
        // 1) 用户主查询（Lambda）
        LambdaQueryWrapper<SysUser> qw = Wrappers.<SysUser>lambdaQuery()
                .like(StrUtil.isNotBlank(req.getUsername()), SysUser::getUsername, req.getUsername())
                .like(StrUtil.isNotBlank(req.getPhone()), SysUser::getPhone, req.getPhone())
                .like(StrUtil.isNotBlank(req.getName()), SysUser::getName, req.getName())
                .in(CollUtil.isNotEmpty(req.getIds()), SysUser::getUserId, req.getIds())
                .eq(req.getDeptId() != null, SysUser::getDeptId, req.getDeptId());

        // 角色筛选（子查询 inSql）
        if (req.getRoleId() != null) {
            qw.inSql(SysUser::getUserId,
                    "select ur.user_id from sys_user_role ur where ur.role_id = " + req.getRoleId());
        }
        if (StrUtil.isNotBlank(req.getRoleCode())) {
            String like = "%" + req.getRoleCode() + "%";
            qw.inSql(SysUser::getUserId,
                    "select ur.user_id from sys_user_role ur " +
                            "join sys_role r on r.role_id = ur.role_id " +
                            "where r.role_code like '" + like + "'");
        }
        if (StrUtil.isNotBlank(req.getRoleName())) {
            String like = "%" + req.getRoleName() + "%";
            qw.inSql(SysUser::getUserId,
                    "select ur.user_id from sys_user_role ur " +
                            "join sys_role r on r.role_id = ur.role_id " +
                            "where r.role_name like '" + like + "'");
        }

        qw.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = sysUserService.page((Page<SysUser>) page, qw);
        List<SysUser> users = userPage.getRecords();
        if (CollUtil.isEmpty(users)) {
            Page<UserVO> empty = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
            empty.setRecords(Collections.emptyList());
            return empty;
        }

        // 2) 批量装配部门（单部门 -> 填充到 List<SysDept>）
        Map<Long, List<SysDept>> userDeptMap = buildUserDeptMap(users);

        // 3) 批量装配角色
        Map<Long, List<SysRole>> userRoleMap = buildUserRoleMap(
                users.stream().map(SysUser::getUserId).toList()
        );

        // 4) 组装 UserVO
        List<UserVO> voList = users.stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtil.copyProperties(u, vo);
            vo.setDeptList(userDeptMap.getOrDefault(u.getUserId(), Collections.emptyList()));
            vo.setRoleList(userRoleMap.getOrDefault(u.getUserId(), Collections.emptyList()));
            // 岗位不处理
            return vo;
        }).toList();

        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 从用户记录里收集 deptId，批量查询部门，并映射到每个用户的 deptList（0或1个）
     */
    private Map<Long, List<SysDept>> buildUserDeptMap(List<SysUser> users) {
        // 收集有效的 deptId
        List<Long> deptIds = users.stream()
                .map(SysUser::getDeptId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }

        // 批量查部门
        Map<Long, SysDept> deptMap = sysDeptService.listByIds(deptIds).stream()
                .collect(Collectors.toMap(SysDept::getDeptId, Function.identity(), (a, b) -> a));

        // userId -> List<SysDept>（最多一个）
        Map<Long, List<SysDept>> res = new HashMap<>();
        for (SysUser u : users) {
            Long deptId = u.getDeptId();
            if (deptId == null) continue;
            SysDept dept = deptMap.get(deptId);
            if (dept != null) {
                res.put(u.getUserId(), Collections.singletonList(dept));
            }
        }
        return res;
        // 如果你希望无部门用户也返回空 List，可保持默认的 getOrDefault(..., emptyList())。
    }

    /**
     * 批量查询用户角色并组装
     */
    private Map<Long, List<SysRole>> buildUserRoleMap(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) return Collections.emptyMap();

        List<SysUserRole> rels = sysUserRoleService.list(Wrappers.<SysUserRole>lambdaQuery()
                .in(SysUserRole::getUserId, userIds));
        if (CollUtil.isEmpty(rels)) return Collections.emptyMap();

        List<Long> roleIds = rels.stream().map(SysUserRole::getRoleId).distinct().toList();
        Map<Long, SysRole> roleMap = sysRoleService.listByIds(roleIds).stream()
                .collect(Collectors.toMap(SysRole::getRoleId, Function.identity(), (a, b) -> a));

        Map<Long, List<SysRole>> map = new HashMap<>();
        for (SysUserRole r : rels) {
            map.computeIfAbsent(r.getUserId(), k -> new ArrayList<>())
                    .add(roleMap.get(r.getRoleId()));
        }
        // 过滤掉可能的 null
        map.replaceAll((k, v) -> v.stream().filter(Objects::nonNull).toList());
        return map;
    }
}
