package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.researchPlatform.*;
import com.pig4cloud.pigx.admin.entity.ResearchPlatformEntity;
import com.pig4cloud.pigx.admin.mapper.ResearchPlatformMapper;
import com.pig4cloud.pigx.admin.service.ResearchPlatformService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class ResearchPlatformServiceImpl extends ServiceImpl<ResearchPlatformMapper, ResearchPlatformEntity> implements ResearchPlatformService {

    @Override
    public IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request) {
        LambdaQueryWrapper<ResearchPlatformEntity> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        if (StrUtil.isNotBlank(request.getKeyword())) {
            wrapper.like(ResearchPlatformEntity::getName, request.getKeyword())
                    .or()
                    .like(ResearchPlatformEntity::getCode, request.getKeyword());
        }
        if (request.getShelfStatus() != null) {
            wrapper.eq(ResearchPlatformEntity::getShelfStatus, request.getShelfStatus());
        }
        if (StrUtil.isNotBlank(request.getBeginTime())) {
            wrapper.ge(ResearchPlatformEntity::getCreateTime, request.getBeginTime());
        }
        if (StrUtil.isNotBlank(request.getEndTime())) {
            wrapper.le(ResearchPlatformEntity::getCreateTime, request.getEndTime());
        }

        // 应用数据权限
        IPage<ResearchPlatformEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        // 转换为响应对象
        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, ResearchPlatformResponse.class));
    }

    @Override
    public ResearchPlatformResponse getDetail(Long id) {
        ResearchPlatformEntity entity = getById(id);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, ResearchPlatformResponse.class);
    }

    @Override
    @Transactional
    public Boolean createPlatform(ResearchPlatformCreateRequest request) {
        ResearchPlatformEntity entity = BeanUtil.copyProperties(request, ResearchPlatformEntity.class);
        entity.setCode("RP" + IdUtil.getSnowflakeNextIdStr());
        return save(entity);
    }

    @Override
    @Transactional
    public Boolean updatePlatform(ResearchPlatformUpdateRequest request) {
        ResearchPlatformEntity entity = getById(request.getId());
        if (entity == null) {
            return false;
        }
        BeanUtil.copyProperties(request, entity);
        return updateById(entity);
    }

    @Override
    @Transactional
    public Boolean removePlatforms(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    @Transactional
    public Boolean changeShelfStatus(ResearchPlatformShelfRequest request) {
        ResearchPlatformEntity entity = getById(request.getId());
        if (entity == null) {
            return false;
        }
        entity.setShelfStatus(request.getShelfStatus());
        return updateById(entity);
    }
}
