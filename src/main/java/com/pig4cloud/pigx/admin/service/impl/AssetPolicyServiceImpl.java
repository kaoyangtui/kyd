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
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.AssetPolicyMapper;
import com.pig4cloud.pigx.admin.service.AssetPolicyService;
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
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(AssetPolicyEntity::getTitle, request.getKeyword())
                        .or()
                        .like(AssetPolicyEntity::getSource, request.getKeyword())
                        .or()
                        .like(AssetPolicyEntity::getContent, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), AssetPolicyEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), AssetPolicyEntity::getDeptId, request.getDeptId());
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

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(AssetPolicyEntity::getCreateTime);
        }

        IPage<AssetPolicyEntity> resultPage = baseMapper.selectPage(page, wrapper);
        return resultPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public AssetPolicyResponse getDetail(Long id) {
        AssetPolicyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(AssetPolicyEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return convertToResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPolicy(AssetPolicyCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePolicy(AssetPolicyUpdateRequest request) {
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removePolicies(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(AssetPolicyCreateRequest request, boolean isCreate) {
        AssetPolicyEntity entity = BeanUtil.copyProperties(request, AssetPolicyEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof AssetPolicyUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private AssetPolicyResponse convertToResponse(AssetPolicyEntity entity) {
        AssetPolicyResponse res = BeanUtil.copyProperties(entity, AssetPolicyResponse.class);
        res.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return res;
    }
}