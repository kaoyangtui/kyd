package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.mapper.DemandReceiveMapper;
import com.pig4cloud.pigx.admin.service.DemandReceiveService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
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

    @Override
    public IPage<DemandReceiveResponse> pageResult(Page reqPage, DemandReceivePageRequest request) {
        LambdaQueryWrapper<DemandReceiveEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DemandReceiveEntity::getReceiveUserId, SecurityUtils.getUser().getId());
        // 根据 ID 进行查询
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(DemandReceiveEntity::getId, request.getIds());
        } else {
            // 根据需求名称进行模糊查询
            wrapper.and(StrUtil.isNotBlank(request.getName()), w ->
                    w.like(DemandReceiveEntity::getName, request.getName())
            );
            // 根据需求类型查询
            wrapper.eq(StrUtil.isNotBlank(request.getType()), DemandReceiveEntity::getType, request.getType());
            // 根据所属领域查询
            wrapper.eq(StrUtil.isNotBlank(request.getField()), DemandReceiveEntity::getField, request.getField());
            // 根据创建人查询
            wrapper.eq(StrUtil.isNotBlank(request.getCreateUserId()), DemandReceiveEntity::getCreateUserId, request.getCreateUserId());
            // 根据提交时间查询
            wrapper.ge(StrUtil.isNotBlank(request.getStartTime()), DemandReceiveEntity::getCreateTime, request.getStartTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), DemandReceiveEntity::getCreateTime, request.getEndTime());
        }

        // 如果存在 startNo 和 endNo，则设置分页
        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }
        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(DemandReceiveEntity::getCreateTime);
        }
        // 查询数据库并返回结果
        IPage<DemandReceiveEntity> entityPage = baseMapper.selectPage(reqPage, wrapper);

        // 转换成响应对象
        return entityPage.convert(this::convertToResponse);
    }

    private DemandReceiveResponse convertToResponse(DemandReceiveEntity entity) {
        DemandReceiveResponse response = new DemandReceiveResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setType(entity.getType());
        response.setField(entity.getField());
        response.setValidStart(entity.getValidStart());
        response.setValidEnd(entity.getValidEnd());
        response.setBudget(entity.getBudget());
        response.setDescription(entity.getDescription());
        response.setCreateUserName(entity.getCreateUserName());
        response.setDeptName(entity.getDeptName());
        response.setCreateTime(entity.getCreateTime());
        return response;
    }

}