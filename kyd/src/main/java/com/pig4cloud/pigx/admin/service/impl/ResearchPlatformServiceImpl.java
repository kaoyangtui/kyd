package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.ShelfRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.*;
import com.pig4cloud.pigx.admin.entity.ResearchPlatformEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchPlatformMapper;
import com.pig4cloud.pigx.admin.service.ResearchPlatformService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ResearchPlatformServiceImpl extends ServiceImpl<ResearchPlatformMapper, ResearchPlatformEntity>
        implements ResearchPlatformService {

    @Override
    @Transactional
    public ResearchPlatformResponse create(ResearchPlatformCreateRequest request) {
        ResearchPlatformEntity entity = new ResearchPlatformEntity();
        entity.setName(request.getName());
        entity.setDirection(request.getDirection());
        entity.setIntro(request.getIntro());
        entity.setContactName(request.getContactName());
        entity.setContactPhone(request.getContactPhone());
        entity.setPrincipal(request.getPrincipal());
        entity.setShelfStatus(request.getShelfStatus());
        this.save(entity);
        return toResponse(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(ResearchPlatformUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        ResearchPlatformEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setName(request.getName());
        entity.setDirection(request.getDirection());
        entity.setIntro(request.getIntro());
        entity.setContactName(request.getContactName());
        entity.setContactPhone(request.getContactPhone());
        entity.setPrincipal(request.getPrincipal());
        entity.setShelfStatus(request.getShelfStatus());
        return this.updateById(entity);
    }

    @Override
    public IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request) {
        LambdaQueryWrapper<ResearchPlatformEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ObjectUtil.isNotEmpty(request.getIds()), ResearchPlatformEntity::getId, request.getIds());
        wrapper.eq(ObjectUtil.isNotEmpty(request.getName()), ResearchPlatformEntity::getName, request.getName());
        wrapper.eq(ObjectUtil.isNotEmpty(request.getPrincipal()), ResearchPlatformEntity::getPrincipal, request.getPrincipal());
        wrapper.eq(ObjectUtil.isNotEmpty(request.getShelfStatus()), ResearchPlatformEntity::getShelfStatus, request.getShelfStatus());
        IPage<ResearchPlatformEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::toResponse);
    }

    @SneakyThrows
    @Override
    public ResearchPlatformResponse getDetail(Long id) {
        ResearchPlatformEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return toResponse(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean updateShelfStatus(ShelfRequest request) {
        Long id = request.getId();
        Integer shelfStatus = request.getShelfStatus();
        if (ObjectUtil.isNull(id)) {
            throw new BizException("ID不能为空");
        }
        ResearchPlatformEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setShelfStatus(shelfStatus);
        return this.updateById(entity);
    }

    @Override
    @Transactional
    public Boolean remove(IdListRequest request) {
        return this.removeBatchByIds(request.getIds());
    }

    private ResearchPlatformResponse toResponse(ResearchPlatformEntity entity) {
        ResearchPlatformResponse response = new ResearchPlatformResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDirection(entity.getDirection());
        response.setIntro(entity.getIntro());
        response.setContactName(entity.getContactName());
        response.setContactPhone(entity.getContactPhone());
        response.setPrincipal(entity.getPrincipal());
        response.setShelfStatus(entity.getShelfStatus());
        response.setCreateBy(entity.getCreateBy());
        response.setCreateTime(entity.getCreateTime());
        response.setDeptId(entity.getDeptId());
        return response;
    }
}
