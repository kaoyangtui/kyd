package com.pig4cloud.pigx.admin.service.impl;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.entity.BmxxEntity;
import com.pig4cloud.pigx.admin.entity.JzgjbxxToZscqEntity;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SyncDataServiceImpl implements SyncDataService {

    private final SysDeptService sysDeptService;
    private final BmxxService bmxxService;
    private final SysUserService sysUserService;
    private final JzgjbxxToZscqService jzgjbxxToZscqService;

    /**
     * 同步校内部门数据到系统部门表，已存在则更新，不存在则插入
     */
    @Transactional
    @Override
    public void syncDepts() {

        // 1. 查询校内部门数据（v_bmxx 视图/表）
        List<BmxxEntity> vDepts = bmxxService.lambdaQuery().list();

        // 2. 查询所有校内部门编码
        Set<String> allCodes = vDepts.stream()
                .map(BmxxEntity::getBmdm)
                .collect(Collectors.toSet());

        // 3. 查询系统表中已存在的部门（根据 code 匹配）
        Map<String, SysDept> existDeptMap = sysDeptService.lambdaQuery()
                .in(SysDept::getCode, allCodes)
                .list()
                .stream()
                .collect(Collectors.toMap(SysDept::getCode, d -> d));

        // 4. 批量插入不存在的，批量更新已存在的
        List<SysDept> needInsert = new ArrayList<>();
        List<SysDept> needUpdate = new ArrayList<>();

        for (BmxxEntity v : vDepts) {
            SysDept dept = new SysDept();
            dept.setCode(v.getBmdm());
            dept.setName(v.getBmmc());
            // 初始 parentId，后面再统一补充
            dept.setParentId(null);
            dept.setDelFlag("0");
            dept.setSortOrder(0);
            dept.setCreateBy("system");
            dept.setUpdateBy("system");
            dept.setCreateTime(LocalDateTime.now());
            dept.setUpdateTime(LocalDateTime.now());

            if (existDeptMap.containsKey(v.getBmdm())) {
                // 已存在，需更新（保留原 deptId）
                SysDept old = existDeptMap.get(v.getBmdm());
                dept.setDeptId(old.getDeptId());
                needUpdate.add(dept);
            } else {
                // 不存在，需插入
                needInsert.add(dept);
            }
        }

        // 5. 批量插入和批量更新
        if (!needInsert.isEmpty()) sysDeptService.saveBatch(needInsert);
        if (!needUpdate.isEmpty()) sysDeptService.updateBatchById(needUpdate);

        // 6. 再查一次（取所有同步后的最新数据，含自增ID），用于 parentId 填充
        List<SysDept> allDepts = sysDeptService.lambdaQuery()
                .in(SysDept::getCode, allCodes)
                .list();

        Map<String, Long> codeToIdMap = allDepts.stream()
                .collect(Collectors.toMap(SysDept::getCode, SysDept::getDeptId));

        // 7. 补 parentId
        for (SysDept dept : allDepts) {
            Optional<BmxxEntity> vDeptOpt = vDepts.stream()
                    .filter(v -> Objects.equals(v.getBmdm(), dept.getCode()))
                    .findFirst();

            if (vDeptOpt.isPresent()) {
                String parentCode = vDeptOpt.get().getFbmbh();
                if (parentCode != null && codeToIdMap.containsKey(parentCode)) {
                    dept.setParentId(codeToIdMap.get(parentCode));
                } else {
                    dept.setParentId(null);
                }
            }
        }

        // 8. 批量更新 parentId
        sysDeptService.updateBatchById(allDepts);
    }

    @Override
    @Transactional
    public void syncUsers() {
        // 1. 查询校内教职工基础数据
        List<JzgjbxxToZscqEntity> jzgList = jzgjbxxToZscqService.lambdaQuery().list();

        // 2. 收集所有教职工涉及的部门编号
        Set<String> deptCodes = jzgList.stream()
                .map(JzgjbxxToZscqEntity::getBmbh)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3. 查询所有相关部门，code->deptId 映射
        Map<String, SysDept> deptCodeMap = sysDeptService.lambdaQuery()
                .in(SysDept::getCode, deptCodes)
                .list()
                .stream()
                .collect(Collectors.toMap(SysDept::getCode, d -> d));

        // 4. 收集所有工号（code），查询已存在用户
        Set<String> userCodes = jzgList.stream()
                .map(JzgjbxxToZscqEntity::getGh)
                .collect(Collectors.toSet());

        Map<String, SysUser> existUserMap = sysUserService.lambdaQuery()
                .in(SysUser::getCode, userCodes)
                .list()
                .stream()
                .collect(Collectors.toMap(SysUser::getCode, u -> u));

        List<SysUser> needInsert = new ArrayList<>();
        List<SysUser> needUpdate = new ArrayList<>();

        for (JzgjbxxToZscqEntity jzg : jzgList) {
            SysUser user = new SysUser();
            // 工号
            user.setCode(jzg.getGh());
            // 用户名用工号，也可自定义
            user.setUsername(jzg.getGh());
            // 姓名
            user.setName(jzg.getXm());
            // 昵称也设为姓名，需调整可自定义
            user.setNickname(jzg.getXm());
            user.setPhone(jzg.getYddh());
            user.setDeptName(jzg.getBmmc());
            user.setPositionTitle(jzg.getZc());
            user.setJobType(jzg.getZglx());
            user.setDelFlag("0");
            user.setCreateBy("system");
            user.setUpdateBy("system");
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setLockFlag("0");
            user.setTenantId(1L); // 如果有多租户场景，可自定义传入

            // 密码/盐等初始化方案（可视需求定制）
            user.setPassword(""); // 置空或默认值
            user.setSalt("");     // 置空或默认值

            // 部门ID转换
            String deptCode = jzg.getBmbh();
            if (deptCode != null && deptCodeMap.containsKey(deptCode)) {
                user.setDeptId(deptCodeMap.get(deptCode).getDeptId());
            } else {
                user.setDeptId(null); // 未找到部门可设为null，或按业务要求处理
            }

            // 已存在则更新，不存在则插入
            if (existUserMap.containsKey(jzg.getGh())) {
                SysUser old = existUserMap.get(jzg.getGh());
                user.setUserId(old.getUserId());
                needUpdate.add(user);
            } else {
                needInsert.add(user);
            }
        }

        // 5. 批量插入和更新
        if (!needInsert.isEmpty()) sysUserService.saveBatch(needInsert);
        if (!needUpdate.isEmpty()) sysUserService.updateBatchById(needUpdate);
    }
}
