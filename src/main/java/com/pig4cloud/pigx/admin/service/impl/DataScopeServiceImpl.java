package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDataScopeService;
import com.pig4cloud.pigx.admin.service.DataScopeService;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhaoliang
 */
@RequiredArgsConstructor
@Service
public class DataScopeServiceImpl implements DataScopeService {

    private final RemoteDataScopeService dataScopeService;

    @Override
    public Boolean calcScope(DataScope dataScope) {
        PigxUser user = SecurityUtils.getUser();

        // 获取用户角色ID列表
        List<Long> roleIdList = SecurityUtils.getRoleIds();
        if (CollUtil.isEmpty(roleIdList)) {
            return false;
        }

        // 获取角色列表
        List<SysRole> roleList = RetOps.of(dataScopeService.getRoleList(roleIdList))
                .getData()
                .orElseGet(Collections::emptyList);
        if (CollUtil.isEmpty(roleList)) {
            return false;
        }

        // 处理数据权限
        return processDataScope(user, dataScope, roleList);
    }


    /**
     * 处理数据权限
     */
    private boolean processDataScope(PigxUser user, DataScope dataScope, List<SysRole> roleList) {
        List<Long> deptList = dataScope.getDeptList();

        for (SysRole role : roleList) {
            Integer dsType = role.getDsType();

            // 处理不同数据权限类型
            switch (Objects.requireNonNull(DataScopeTypeEnum.getByType(dsType))) {
                case ALL:
                    return true;
                case CUSTOM:
                    handleCustomScope(role, deptList);
                    break;
                case OWN_CHILD_LEVEL:
                    handleOwnChildLevelScope(user, deptList);
                    break;
                case OWN_LEVEL:
                    handleOwnLevelScope(user, deptList);
                    break;
                case SELF_LEVEL:
                    handleSelfLevelScope(user, dataScope);
                    break;
                default:
                    break;
            }
        }

        return false;
    }


    /**
     * 处理自定义数据权限
     */
    private void handleCustomScope(SysRole role, List<Long> deptList) {
        if (StrUtil.isNotBlank(role.getDsScope())) {
            deptList.addAll(Arrays.stream(role.getDsScope().split(StrUtil.COMMA))
                    .map(Long::parseLong)
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 处理本级及下级数据权限
     */
    private void handleOwnChildLevelScope(PigxUser user, List<Long> deptList) {
        List<Long> descendantDeptIds = RetOps.of(dataScopeService.getDescendantList(user.getDeptId()))
                .getData()
                .orElseGet(Collections::emptyList)
                .stream()
                .map(SysDept::getDeptId)
                .collect(Collectors.toList());
        deptList.addAll(descendantDeptIds);
    }

    /**
     * 处理本级数据权限
     */
    private void handleOwnLevelScope(PigxUser user, List<Long> deptList) {
        deptList.add(user.getDeptId());
    }

    /**
     * 处理个人数据权限
     */
    private void handleSelfLevelScope(PigxUser user, DataScope dataScope) {
        dataScope.setUsername(user.getName());
    }
}
