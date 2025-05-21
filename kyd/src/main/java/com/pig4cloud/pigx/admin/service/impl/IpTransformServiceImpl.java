package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.IpTransformMapper;
import com.pig4cloud.pigx.admin.service.IpTransformService;
import com.pig4cloud.pigx.admin.dto.ipTransform.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IpTransformServiceImpl extends ServiceImpl<IpTransformMapper, IpTransformEntity> implements IpTransformService {

    @Override
    @Transactional
    public Boolean create(IpTransformCreateRequest request) {
        IpTransformEntity entity = BeanUtil.copyProperties(request, IpTransformEntity.class);
        entity.setIpCode(StrUtil.join(";", request.getIpCode()));
        entity.setConsentFileUrl(StrUtil.join(";", request.getConsentFileUrl()));
        entity.setPromiseFileUrl(StrUtil.join(";", request.getPromiseFileUrl()));
        return this.save(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(IpTransformUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        IpTransformEntity entity = BeanUtil.copyProperties(request, IpTransformEntity.class);
        entity.setIpCode(StrUtil.join(";", request.getIpCode()));
        entity.setConsentFileUrl(StrUtil.join(";", request.getConsentFileUrl()));
        entity.setPromiseFileUrl(StrUtil.join(";", request.getPromiseFileUrl()));
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public IpTransformResponse getDetail(Long id) {
        IpTransformEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        IpTransformResponse response = BeanUtil.copyProperties(entity, IpTransformResponse.class);
        response.setIpCode(StrUtil.split(entity.getIpCode(), ";"));
        response.setConsentFileUrl(StrUtil.split(entity.getConsentFileUrl(), ";"));
        response.setPromiseFileUrl(StrUtil.split(entity.getPromiseFileUrl(), ";"));
        return response;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<IpTransformResponse> pageResult(Page page, IpTransformPageRequest request) {
        LambdaQueryWrapper<IpTransformEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IpTransformEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), IpTransformEntity::getName, request.getKeyword())
                    .or().like(StrUtil.isNotBlank(request.getKeyword()), IpTransformEntity::getCode, request.getKeyword());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IpTransformEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IpTransformEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), IpTransformEntity::getDeptId, request.getCreateByDept());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IpTransformEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IpTransformEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        Page<IpTransformEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(entity -> {
            IpTransformResponse response = BeanUtil.copyProperties(entity, IpTransformResponse.class);
            response.setIpCode(StrUtil.split(entity.getIpCode(), ";"));
            response.setConsentFileUrl(StrUtil.split(entity.getConsentFileUrl(), ";"));
            response.setPromiseFileUrl(StrUtil.split(entity.getPromiseFileUrl(), ";"));
            return response;
        });
    }
}