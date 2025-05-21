package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.StandardDrafterInEntity;
import com.pig4cloud.pigx.admin.entity.StandardEntity;
import com.pig4cloud.pigx.admin.entity.StandardOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.StandardMapper;
import com.pig4cloud.pigx.admin.service.StandardDrafterInService;
import com.pig4cloud.pigx.admin.service.StandardOwnerService;
import com.pig4cloud.pigx.admin.service.StandardService;
import com.pig4cloud.pigx.admin.dto.standard.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandardServiceImpl extends ServiceImpl<StandardMapper, StandardEntity> implements StandardService {

    private final StandardOwnerService ownerService;
    private final StandardDrafterInService drafterInService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createStandard(StandardCreateRequest request) {
        StandardEntity entity = BeanUtil.copyProperties(request.getMain(), StandardEntity.class);
        this.save(entity);
        ownerService.replaceOwners(entity.getId(), request.getOwners());
        drafterInService.replaceDrafters(entity.getId(), request.getDrafters());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStandard(StandardUpdateRequest request) {
        StandardEntity entity = BeanUtil.copyProperties(request.getMain(), StandardEntity.class);
        this.updateById(entity);
        ownerService.replaceOwners(entity.getId(), request.getOwners());
        drafterInService.replaceDrafters(entity.getId(), request.getDrafters());
        return Boolean.TRUE;
    }

    @Override
    public StandardResponse getDetail(Long id) {
        StandardEntity entity = this.getById(id);
        StandardResponse response = new StandardResponse();
        response.setMain(BeanUtil.copyProperties(entity, StandardMainVO.class));
        response.setOwners(ownerService.lambdaQuery().eq(StandardOwnerEntity::getStandardId, id).list().stream().map(o -> BeanUtil.copyProperties(o, StandardOwnerVO.class)).collect(Collectors.toList()));
        response.setDrafters(drafterInService.lambdaQuery().eq(StandardDrafterInEntity::getStandardId, id).list().stream().map(d -> BeanUtil.copyProperties(d, StandardDrafterInVO.class)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public IPage<StandardResponse> pageResult(Page reqPage, StandardPageRequest request) {
        LambdaQueryWrapper<StandardEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w -> w.like(StandardEntity::getCode, request.getKeyword()).or().like(StandardEntity::getName, request.getKeyword()));
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), StandardEntity::getDeptId, request.getDeptId());
        wrapper.ge(StrUtil.isNotBlank(request.getPubBeginTime()), StandardEntity::getPubDate, request.getPubBeginTime());
        wrapper.le(StrUtil.isNotBlank(request.getPubEndTime()), StandardEntity::getPubDate, request.getPubEndTime());
        wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), StandardEntity::getFlowStatus, request.getFlowStatus());
        wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), StandardEntity::getCurrentNodeName, request.getCurrentNodeName());

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<StandardEntity> page = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());

        return page.convert(entity -> {
            StandardResponse res = new StandardResponse();
            res.setMain(BeanUtil.copyProperties(entity, StandardMainVO.class));
            res.setOwners(ownerService.lambdaQuery().eq(StandardOwnerEntity::getStandardId, entity.getId()).list().stream().map(o -> BeanUtil.copyProperties(o, StandardOwnerVO.class)).collect(Collectors.toList()));
            res.setDrafters(drafterInService.lambdaQuery().eq(StandardDrafterInEntity::getStandardId, entity.getId()).list().stream().map(d -> BeanUtil.copyProperties(d, StandardDrafterInVO.class)).collect(Collectors.toList()));
            return res;
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeStandards(List<Long> ids) {
        this.removeBatchByIds(ids);
        ownerService.removeByStandardIds(ids);
        drafterInService.removeByStandardIds(ids);
        return Boolean.TRUE;
    }

}
