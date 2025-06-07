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
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyCreateRequest;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyPageRequest;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyResponse;
import com.pig4cloud.pigx.admin.dto.assetPolicy.AssetPolicyUpdateRequest;
import com.pig4cloud.pigx.admin.entity.AssetPolicyEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.AssetPolicyMapper;
import com.pig4cloud.pigx.admin.service.AssetPolicyService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetPolicyServiceImpl extends ServiceImpl<AssetPolicyMapper, AssetPolicyEntity> implements AssetPolicyService {

    @Override
    public IPage<AssetPolicyResponse> pageResult(Page page, AssetPolicyPageRequest request) {
        LambdaQueryWrapper<AssetPolicyEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(AssetPolicyEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), AssetPolicyEntity::getTitle, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), AssetPolicyEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), AssetPolicyEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), AssetPolicyEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<AssetPolicyEntity> resultPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resultPage.convert(entity -> {
            AssetPolicyResponse res = BeanUtil.copyProperties(entity, AssetPolicyResponse.class);
            res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
            return res;
        });
    }

    @Override
    @SneakyThrows
    public AssetPolicyResponse getDetail(Long id) {
        AssetPolicyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        AssetPolicyResponse res = BeanUtil.copyProperties(entity, AssetPolicyResponse.class);
        res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPolicy(AssetPolicyCreateRequest request) {
        AssetPolicyEntity entity = BeanUtil.copyProperties(request, AssetPolicyEntity.class);
        entity.setId(null);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        this.save(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePolicy(AssetPolicyUpdateRequest request) {
        AssetPolicyEntity entity = BeanUtil.copyProperties(request, AssetPolicyEntity.class);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        this.updateById(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removePolicies(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }
}
