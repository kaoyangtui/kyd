package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistCreateRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistPageRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistResponse;
import com.pig4cloud.pigx.admin.dto.blacklist.BlacklistUpdateRequest;
import com.pig4cloud.pigx.admin.entity.BlacklistEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.BlacklistMapper;
import com.pig4cloud.pigx.admin.service.BlacklistService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, BlacklistEntity> implements BlacklistService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlacklistResponse createBlacklist(BlacklistCreateRequest request) {
        BlacklistEntity entity = CopyUtil.copyProperties(request, BlacklistEntity.class);
        this.save(entity);
        return convertToResponse(entity);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBlacklist(BlacklistUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        BlacklistEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("黑名单记录不存在");
        }
        CopyUtil.copyProperties(request, entity);
        this.updateById(entity);
        return true;
    }

    @Override
    public IPage<BlacklistResponse> pageResult(Page reqPage, BlacklistPageRequest request) {
        LambdaQueryWrapper<BlacklistEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(BlacklistEntity::getId, request.getIds());
        } else {
            wrapper.eq(ObjectUtil.isNotNull(request.getUserId()), BlacklistEntity::getUserId, request.getUserId());
            wrapper.like(ObjectUtil.isNotNull(request.getUserName()), BlacklistEntity::getUserName, request.getUserName());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }
        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(BlacklistEntity::getId);
        }
        IPage<BlacklistEntity> entityPage = baseMapper.selectPage(reqPage, wrapper);
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public Boolean removeBlacklist(IdListRequest request) {
        List<Long> ids = request.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }
        return this.removeBatchByIds(ids);
    }

    private BlacklistResponse convertToResponse(BlacklistEntity entity) {
        return CopyUtil.copyProperties(entity, BlacklistResponse.class);
    }
}
