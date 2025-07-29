package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.mapper.DemandReceiveMapper;
import com.pig4cloud.pigx.admin.service.DemandReceiveService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 企业需求接收表
 *
 * @author pigx
 * @date 2025-07-29 12:33:46
 */
@RequiredArgsConstructor
@Service
public class DemandReceiveServiceImpl extends ServiceImpl<DemandReceiveMapper, DemandReceiveEntity> implements DemandReceiveService {

    private final DemandMapper demandMapper;
    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;

    @Override
    public Boolean receive(DemandReceiveRequest request) {
        DemandEntity demand = demandMapper.selectById(request.getDemandId());
        DemandReceiveEntity demandReceiveEntity = BeanUtil.copyProperties(demand, DemandReceiveEntity.class);
        demandReceiveEntity.setDemandId(demand.getId());
        demandReceiveEntity.setReceiveUserId(request.getUserId());
        SysUser sysUser = sysUserService.getById(request.getUserId());
        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        demandReceiveEntity.setDeptId(String.valueOf(sysDept.getDeptId()));
        demandReceiveEntity.setDeptName(sysDept.getName());
        demandReceiveEntity.setCreateUserId(sysUser.getUserId());
        demandReceiveEntity.setCreateUserName(sysUser.getUsername());
        return this.save(demandReceiveEntity);
    }
}