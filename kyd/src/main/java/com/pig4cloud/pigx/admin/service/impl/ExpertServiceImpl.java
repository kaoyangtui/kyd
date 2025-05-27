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
import com.pig4cloud.pigx.admin.dto.expert.*;
import com.pig4cloud.pigx.admin.entity.ExpertEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ExpertMapper;
import com.pig4cloud.pigx.admin.service.ExpertService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ExpertEntity::getName, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), ExpertEntity::getDeptId, request.getDeptId());
        }

        // 范围/区间分页
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<ExpertEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, ExpertResponse.class));
    }

    @SneakyThrows
    @Override
    public ExpertResponse getDetail(Long id) {
        ExpertEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("专家数据不存在");
        }
        return BeanUtil.copyProperties(entity, ExpertResponse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ExpertCreateRequest request) {
        ExpertEntity entity = BeanUtil.copyProperties(request, ExpertEntity.class);
        this.save(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ExpertUpdateRequest request) {
        ExpertEntity entity = BeanUtil.copyProperties(request, ExpertEntity.class);
        this.updateById(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public List<ExpertResponse> exportList(ExpertPageRequest request) {
        LambdaQueryWrapper<ExpertEntity> wrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ExpertEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ExpertEntity::getName, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), ExpertEntity::getDeptId, request.getDeptId());
        }
        List<ExpertEntity> list = this.list(wrapper);
        return BeanUtil.copyToList(list, ExpertResponse.class);
    }

    @Override
    public com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse exportFields() {
        // 通过工具类生成字段列表（实际实现请按你们项目的 ExportFieldHelper 工具）
        return com.pig4cloud.pigx.admin.utils.ExportFieldHelper.buildExportFieldList(
                ExpertResponse.BIZ_CODE,
                ExpertResponse.class
        );
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
                .set(ExpertEntity::getShelfStatus, shelfStatus));
    }

}
