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
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.*;
import com.pig4cloud.pigx.admin.entity.ResearchNewsEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchNewsMapper;
import com.pig4cloud.pigx.admin.service.ResearchNewsService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科研动态管理 ServiceImpl
 *
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class ResearchNewsServiceImpl extends ServiceImpl<ResearchNewsMapper, ResearchNewsEntity> implements ResearchNewsService {

    @Override
    public IPage<ResearchNewsResponse> pageResult(Page page, ResearchNewsPageRequest request) {
        LambdaQueryWrapper<ResearchNewsEntity> wrapper = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchNewsEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ResearchNewsEntity::getTitle, request.getKeyword());
            wrapper.like(StrUtil.isNotBlank(request.getProvider()), ResearchNewsEntity::getProvider, request.getProvider());
        }
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }
        IPage<ResearchNewsEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, ResearchNewsResponse.class));
    }

    @SneakyThrows
    @Override
    public ResearchNewsResponse getDetail(Long id) {
        ResearchNewsEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        ResearchNewsResponse response = BeanUtil.copyProperties(entity, ResearchNewsResponse.class);

        response.setFileUrl(StrUtil.isNotBlank(entity.getFileUrl())
                ? StrUtil.split(entity.getFileUrl(), ";")
                : null);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNews(ResearchNewsCreateRequest request) {
        ResearchNewsEntity entity = BeanUtil.copyProperties(request, ResearchNewsEntity.class);
        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateNews(ResearchNewsUpdateRequest request) {
        ResearchNewsEntity entity = BeanUtil.copyProperties(request, ResearchNewsEntity.class);
        entity.setId(request.getId());
        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeNews(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public ExportFieldListResponse exportFields() {
        return ExportFieldHelper.buildExportFieldList(
                ResearchNewsResponse.BIZ_CODE,
                ResearchNewsResponse.class
        );
    }
}
