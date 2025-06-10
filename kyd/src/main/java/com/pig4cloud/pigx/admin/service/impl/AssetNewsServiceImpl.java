package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsCreateRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsResponse;
import com.pig4cloud.pigx.admin.dto.assetNews.AssetNewsUpdateRequest;
import com.pig4cloud.pigx.admin.entity.AssetNewsEntity;
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
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(AssetNewsEntity::getTitle, request.getKeyword())
                        .or()
                        .like(AssetNewsEntity::getSource, request.getKeyword())
                        .or()
                        .like(AssetNewsEntity::getContent, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), AssetNewsEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), AssetNewsEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), AssetNewsEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), AssetNewsEntity::getCreateTime, request.getEndTime());
        }

        // 支持 ids、startNo、endNo 特殊分页
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<AssetNewsEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(entity -> {
            AssetNewsResponse res = BeanUtil.copyProperties(entity, AssetNewsResponse.class);
            res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
            return res;
        });
    }

    @SneakyThrows
    @Override
    public AssetNewsResponse getDetail(Long id) {
        AssetNewsEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        AssetNewsResponse res = BeanUtil.copyProperties(entity, AssetNewsResponse.class);
        res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(AssetNewsCreateRequest request) {
        AssetNewsEntity entity = BeanUtil.copyProperties(request, AssetNewsEntity.class);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        this.save(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(AssetNewsUpdateRequest request) {
        AssetNewsEntity entity = BeanUtil.copyProperties(request, AssetNewsEntity.class);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        this.updateById(entity);
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
        List<AssetNewsEntity> list = this.list(wrapper);
        List<AssetNewsResponse> result = list.stream().map(entity -> {
            AssetNewsResponse res = BeanUtil.copyProperties(entity, AssetNewsResponse.class);
            res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
            return res;
        }).toList();
        return result;

    }
}
