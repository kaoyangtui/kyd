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
import com.pig4cloud.pigx.admin.dto.expert.ExpertCreateRequest;
import com.pig4cloud.pigx.admin.dto.expert.ExpertPageRequest;
import com.pig4cloud.pigx.admin.dto.expert.ExpertResponse;
import com.pig4cloud.pigx.admin.dto.expert.ExpertUpdateRequest;
import com.pig4cloud.pigx.admin.entity.ExpertEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ExpertMapper;
import com.pig4cloud.pigx.admin.service.ExpertService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专家信息管理 ServiceImpl
 *
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class ExpertServiceImpl extends ServiceImpl<ExpertMapper, ExpertEntity> implements ExpertService {

    @Override
    public IPage<ExpertResponse> pageResult(Page page, ExpertPageRequest request) {
        LambdaQueryWrapper<ExpertEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ExpertEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(ExpertEntity::getName, request.getKeyword())
                                .or()
                                .like(ExpertEntity::getIntro, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getName()), ExpertEntity::getName, request.getName());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), ExpertEntity::getDeptId, request.getDeptId());
            wrapper.eq(null != request.getShelfStatus(), ExpertEntity::getShelfStatus, request.getShelfStatus());
        }

        // 范围/区间分页
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ExpertEntity::getCreateTime);
        }

        IPage<ExpertEntity> entityPage = baseMapper.selectPage(page, wrapper);

        return entityPage.convert(entity -> CopyUtil.copyProperties(entity, ExpertResponse.class));
    }

    @SneakyThrows
    @Override
    public ExpertResponse getDetail(Long id) {
        ExpertEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("专家数据不存在");
        }
        return CopyUtil.copyProperties(entity, ExpertResponse.class);
    }

    @SneakyThrows
    @Override
    public ExpertResponse getDetailByCode(String code) {
        ExpertEntity entity = this.lambdaQuery().eq(ExpertEntity::getCode, code).one();
        if (entity == null) {
            return null;
        }
        return CopyUtil.copyProperties(entity, ExpertResponse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ExpertCreateRequest request) {
        ExpertEntity entity = CopyUtil.copyProperties(request, ExpertEntity.class);
        this.save(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ExpertUpdateRequest request) {
        ExpertEntity entity = CopyUtil.copyProperties(request, ExpertEntity.class);
        this.updateById(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean shelfByIds(List<Long> ids, Integer shelfStatus) {
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }
        // 约定 shelf_status 字段（如未在表中实际存在，请先DDL同步）
        return this.update(Wrappers.<ExpertEntity>lambdaUpdate()
                .in(ExpertEntity::getId, ids)
                .set(ExpertEntity::getShelfStatus, shelfStatus)
                .set(ExpertEntity::getShelfTime, LocalDateTime.now()));
    }

}
