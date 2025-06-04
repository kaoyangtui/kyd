package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.service.DemandService;
import com.pig4cloud.pigx.admin.dto.demand.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandServiceImpl extends ServiceImpl<DemandMapper, DemandEntity> implements DemandService {

    @Override
    @Transactional
    public Boolean create(DemandCreateRequest request) {
        DemandEntity entity = BeanUtil.copyProperties(request, DemandEntity.class);
        entity.setTags(StrUtil.join(";", request.getTags()));
        entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        return this.save(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(DemandUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        DemandEntity entity = BeanUtil.copyProperties(request, DemandEntity.class);
        entity.setTags(StrUtil.join(";", request.getTags()));
        entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public DemandResponse getDetail(Long id) {
        DemandEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        DemandResponse response = BeanUtil.copyProperties(entity, DemandResponse.class);
        response.setTags(StrUtil.split(entity.getTags(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<DemandResponse> pageResult(Page page, DemandPageRequest request) {
        LambdaQueryWrapper<DemandEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(DemandEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), DemandEntity::getName, request.getKeyword())
                    .or().like(StrUtil.isNotBlank(request.getKeyword()), DemandEntity::getTags, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getType()), DemandEntity::getType, request.getType());
            wrapper.eq(StrUtil.isNotBlank(request.getField()), DemandEntity::getField, request.getField());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), DemandEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), DemandEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), DemandEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), DemandEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), DemandEntity::getCreateTime, request.getEndTime());
            wrapper.eq(null != request.getShelfStatus(), DemandEntity::getShelfStatus, request.getShelfStatus());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<DemandEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(entity -> {
            DemandResponse response = BeanUtil.copyProperties(entity, DemandResponse.class);
            response.setTags(StrUtil.split(entity.getTags(), ";"));
            response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
            return response;
        });
    }
}