package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsCreateRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsResponse;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsUpdateRequest;
import com.pig4cloud.pigx.admin.entity.AssetNewsEntity;
import com.pig4cloud.pigx.admin.entity.TransformCaseEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.AssetNewsMapper;
import com.pig4cloud.pigx.admin.service.AssetNewsService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetNewsServiceImpl extends ServiceImpl<AssetNewsMapper, AssetNewsEntity> implements AssetNewsService {

    @Override
    public IPage<AssetNewsResponse> pageResult(Page page, AssetNewsPageRequest request) {
        LambdaQueryWrapper<AssetNewsEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(AssetNewsEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), AssetNewsEntity::getTitle, request.getKeyword())
                    .or(StrUtil.isNotBlank(request.getKeyword()))
                    .like(AssetNewsEntity::getSource, request.getKeyword())
                    .or(StrUtil.isNotBlank(request.getKeyword()))
                    .like(AssetNewsEntity::getContent, request.getKeyword());

            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), AssetNewsEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), AssetNewsEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), AssetNewsEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), AssetNewsEntity::getCreateTime, request.getEndTime());
        }

        // 支持 startNo/endNo 分页
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(AssetNewsEntity::getCreateTime);
        }

        IPage<AssetNewsEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public AssetNewsResponse getDetail(Long id) {
        AssetNewsEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(AssetNewsEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return convertToResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(AssetNewsCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(AssetNewsUpdateRequest request) {
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public List<AssetNewsResponse> exportList(AssetNewsPageRequest request) {
        LambdaQueryWrapper<AssetNewsEntity> wrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(AssetNewsEntity::getId, request.getIds());
        }
        return this.list(wrapper).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private void doSaveOrUpdate(AssetNewsCreateRequest request, boolean isCreate) {
        AssetNewsEntity entity = BeanUtil.copyProperties(request, AssetNewsEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof AssetNewsUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private AssetNewsResponse convertToResponse(AssetNewsEntity entity) {
        AssetNewsResponse res = BeanUtil.copyProperties(entity, AssetNewsResponse.class);
        res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return res;
    }
}
