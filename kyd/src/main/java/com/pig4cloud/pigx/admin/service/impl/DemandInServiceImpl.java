package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInCreateRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInPageRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInUpdateRequest;
import com.pig4cloud.pigx.admin.entity.DemandInEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.DemandInMapper;
import com.pig4cloud.pigx.admin.service.DemandInService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandInServiceImpl extends ServiceImpl<DemandInMapper, DemandInEntity> implements DemandInService {

    @Override
    @Transactional
    public Boolean create(DemandInCreateRequest request) {
        DemandInEntity entity = BeanUtil.copyProperties(request, DemandInEntity.class);
        entity.setTags(StrUtil.join(";", request.getTags()));
        entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        return this.save(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(DemandInUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        DemandInEntity entity = BeanUtil.copyProperties(request, DemandInEntity.class);
        entity.setTags(StrUtil.join(";", request.getTags()));
        entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public DemandInResponse getDetail(Long id) {
        DemandInEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        DemandInResponse response = BeanUtil.copyProperties(entity, DemandInResponse.class);
        response.setTags(StrUtil.split(entity.getTags(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<DemandInResponse> pageResult(Page page, DemandInPageRequest request) {
        LambdaQueryWrapper<DemandInEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(DemandInEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), DemandInEntity::getName, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getType()), DemandInEntity::getType, request.getType());
            //技术领域
            if (CollUtil.isNotEmpty(request.getField())) {
                wrapper.and(q -> request.getField().forEach(o -> q.or().like(DemandInEntity::getField, o)));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), DemandInEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), DemandInEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), DemandInEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), DemandInEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), DemandInEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), DemandInEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        Page<DemandInEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(entity -> {
            DemandInResponse response = BeanUtil.copyProperties(entity, DemandInResponse.class);
            response.setTags(StrUtil.split(entity.getTags(), ";"));
            response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
            return response;
        });
    }

    @SneakyThrows
    @Transactional
    @Override
    public Boolean updateShelfStatus(Long id, Integer shelfStatus) {
        if (ObjectUtil.isNull(id)) {
            throw new BizException("ID不能为空");
        }
        DemandInEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setShelfStatus(shelfStatus);
        return this.updateById(entity);
    }
}
