package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformPageRequest;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformResponse;
import com.pig4cloud.pigx.admin.dto.researchPlatform.ResearchPlatformUpdateRequest;
import com.pig4cloud.pigx.admin.entity.ResearchNewsEntity;
import com.pig4cloud.pigx.admin.entity.ResearchPlatformEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchPlatformMapper;
import com.pig4cloud.pigx.admin.service.ResearchPlatformService;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchPlatformServiceImpl extends ServiceImpl<ResearchPlatformMapper, ResearchPlatformEntity> implements ResearchPlatformService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ResearchPlatformCreateRequest request) {
        ResearchPlatformEntity entity = BeanUtil.copyProperties(request, ResearchPlatformEntity.class);
        entity.setId(null);
        // 平台研究方向用;分隔字符串保存
        entity.setDirection(StrUtil.join(";", request.getDirection()));
        // 业务主键编码（如需编码可加）
        entity.setCode(ParamResolver.getStr(ResearchPlatformResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        entity.setShelfStatus(0);
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ResearchPlatformUpdateRequest request) {
        ResearchPlatformEntity entity = BeanUtil.copyProperties(request, ResearchPlatformEntity.class);
        entity.setDirection(StrUtil.join(";", request.getDirection()));
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public ResearchPlatformResponse getDetail(Long id) {
        ResearchPlatformEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("科研平台不存在");
        }
        ResearchPlatformResponse response = BeanUtil.copyProperties(entity, ResearchPlatformResponse.class);
        response.setDirection(StrUtil.split(entity.getDirection(), ";"));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean shelfByIds(List<Long> ids, Integer shelfStatus) {
        return this.update(Wrappers.<ResearchPlatformEntity>lambdaUpdate()
                .in(ResearchPlatformEntity::getId, ids)
                .set(ResearchPlatformEntity::getShelfStatus, shelfStatus));
    }

    @Override
    public IPage<ResearchPlatformResponse> pageResult(Page page, ResearchPlatformPageRequest request) {
        LambdaQueryWrapper<ResearchPlatformEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchPlatformEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(ResearchPlatformEntity::getName, request.getKeyword())
                                .or()
                                .like(ResearchPlatformEntity::getDirection, request.getKeyword())
                                .or()
                                .like(ResearchPlatformEntity::getIntro, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getLeaderCode()), ResearchPlatformEntity::getLeaderCode, request.getLeaderCode());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), ResearchPlatformEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResearchPlatformEntity::getShelfStatus, request.getShelfStatus());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ResearchPlatformEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ResearchPlatformEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ResearchPlatformEntity::getCreateTime);
        }

        IPage<ResearchPlatformEntity> entityPage = baseMapper.selectPage(page, wrapper);

        return entityPage.convert(entity -> {
            ResearchPlatformResponse response = BeanUtil.copyProperties(entity, ResearchPlatformResponse.class);
            response.setDirection(StrUtil.split(entity.getDirection(), ";"));
            return response;
        });
    }
}
