package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.consult.*;
import com.pig4cloud.pigx.admin.entity.CommonDownloadEntity;
import com.pig4cloud.pigx.admin.entity.ConsultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ConsultMapper;
import com.pig4cloud.pigx.admin.service.ConsultService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends ServiceImpl<ConsultMapper, ConsultEntity> implements ConsultService {

    @Override
    @Transactional
    public Boolean create(ConsultCreateRequest request) {
        ConsultEntity entity = CopyUtil.copyProperties(request, ConsultEntity.class);
        entity.setStatus(0);
        entity.setReplyStatus(0);
        return this.save(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(ConsultUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        ConsultEntity entity = CopyUtil.copyProperties(request, ConsultEntity.class);
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean reply(ConsultReplyRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        ConsultEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setReplyBy(SecurityUtils.getUser().getNickname());
        entity.setReplyTime(LocalDateTime.now());
        entity.setReplyContent(request.getReplyContent());
        entity.setReplyStatus(1);
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public ConsultResponse getDetail(Long id) {
        ConsultEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        return CopyUtil.copyProperties(entity, ConsultResponse.class);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<ConsultResponse> pageResult(Page page, ConsultPageRequest request) {
        LambdaQueryWrapper<ConsultEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ConsultEntity::getId, request.getIds());
        } else {
            wrapper.eq(null != request.getUserId(), ConsultEntity::getUserId, request.getUserId());
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ConsultEntity::getContent, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getType()), ConsultEntity::getType, request.getType());
            wrapper.eq(ObjectUtil.isNotNull(request.getStatus()), ConsultEntity::getStatus, request.getStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getReplyStatus()), ConsultEntity::getReplyStatus, request.getReplyStatus());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ConsultEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ConsultEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ConsultEntity::getCreateTime);
        }

        IPage<ConsultEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(e -> CopyUtil.copyProperties(e, ConsultResponse.class));
    }

    @SneakyThrows
    @Override
    public ConsultResponse getDetailRead(Long id) {
        ConsultEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        if (null == entity.getStatus() || entity.getStatus() == 0) {
            this.lambdaUpdate()
                    .eq(ConsultEntity::getId, id)
                    .set(ConsultEntity::getStatus, 1)
                    .update();
        }
        return CopyUtil.copyProperties(entity, ConsultResponse.class);
    }
}