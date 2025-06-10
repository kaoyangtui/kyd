package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseCreateRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCasePageRequest;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseResponse;
import com.pig4cloud.pigx.admin.dto.transformCase.TransformCaseUpdateRequest;
import com.pig4cloud.pigx.admin.entity.TransformCaseEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.TransformCaseMapper;
import com.pig4cloud.pigx.admin.service.TransformCaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 转化案例 ServiceImpl
 *
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class TransformCaseServiceImpl extends ServiceImpl<TransformCaseMapper, TransformCaseEntity> implements TransformCaseService {

    @Override
    public IPage<TransformCaseResponse> pageResult(Page page, TransformCasePageRequest request) {
        LambdaQueryWrapper<TransformCaseEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(TransformCaseEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(TransformCaseEntity::getTitle, request.getKeyword())
                                .or()
                                .like(TransformCaseEntity::getContent, request.getKeyword())
                                .or()
                                .like(TransformCaseEntity::getProvider, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), TransformCaseEntity::getCreateBy, request.getCreateBy());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), TransformCaseEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), TransformCaseEntity::getCreateTime, request.getEndTime());
        }

        // 支持 ids 分页和 startNo/endNo 分页扩展，可根据需要补充
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<TransformCaseEntity> entityPage = this.page(page, wrapper);

        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, TransformCaseResponse.class));
    }

    @SneakyThrows
    @Override
    public TransformCaseResponse getDetail(Long id) {
        TransformCaseEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        TransformCaseResponse response = BeanUtil.copyProperties(entity, TransformCaseResponse.class);
        response.setFileUrl(StrUtil.isNotBlank(entity.getFileUrl())
                ? StrUtil.split(entity.getFileUrl(), ";")
                : null);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCase(TransformCaseCreateRequest request) {
        TransformCaseEntity entity = BeanUtil.copyProperties(request, TransformCaseEntity.class);
        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCase(TransformCaseUpdateRequest request) {
        TransformCaseEntity entity = BeanUtil.copyProperties(request, TransformCaseEntity.class);
        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCases(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

}
