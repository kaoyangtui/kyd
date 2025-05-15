package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.entity.IcLayoutOwnerEntity;
import com.pig4cloud.pigx.admin.entity.IcLayoutCreatorInEntity;
import com.pig4cloud.pigx.admin.mapper.IcLayoutMapper;
import com.pig4cloud.pigx.admin.service.IcLayoutOwnerService;
import com.pig4cloud.pigx.admin.service.IcLayoutCreatorInService;
import com.pig4cloud.pigx.admin.service.IcLayoutService;
import com.pig4cloud.pigx.admin.vo.icLayout.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IcLayoutServiceImpl extends ServiceImpl<IcLayoutMapper, IcLayoutEntity> implements IcLayoutService {

    private final IcLayoutOwnerService ownerService;
    private final IcLayoutCreatorInService creatorInService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createLayout(IcLayoutCreateRequest request) {
        IcLayoutEntity entity = BeanUtil.copyProperties(request.getMain(), IcLayoutEntity.class);
        this.save(entity);
        this.replaceOwners(entity.getId(), request.getOwners());
        this.replaceCreators(entity.getId(), request.getCreators());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLayout(IcLayoutUpdateRequest request) {
        IcLayoutEntity entity = BeanUtil.copyProperties(request.getMain(), IcLayoutEntity.class);
        this.updateById(entity);
        this.replaceOwners(entity.getId(), request.getOwners());
        this.replaceCreators(entity.getId(), request.getCreators());
        return Boolean.TRUE;
    }

    @Override
    public IPage<IcLayoutResponse> pageResult(IcLayoutPageRequest request) {
        LambdaQueryWrapper<IcLayoutEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                w.like(IcLayoutEntity::getRegNo, request.getKeyword())
                        .or().like(IcLayoutEntity::getName, request.getKeyword()));
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), IcLayoutEntity::getDeptId, request.getDeptId());
        wrapper.ge(StrUtil.isNotBlank(request.getPublishBeginTime()), IcLayoutEntity::getPublishDate, request.getPublishBeginTime());
        wrapper.le(StrUtil.isNotBlank(request.getPublishEndTime()), IcLayoutEntity::getPublishDate, request.getPublishEndTime());
        wrapper.orderByDesc(IcLayoutEntity::getCreateTime);

        Page<IcLayoutEntity> page = baseMapper.selectPageByScope(new Page<>(request.getCurrent(), request.getSize()), wrapper, DataScope.of());

        return page.convert(entity -> {
            IcLayoutResponse res = new IcLayoutResponse();
            res.setMain(BeanUtil.copyProperties(entity, IcLayoutMainVO.class));
            res.setOwners(ownerService.lambdaQuery().eq(IcLayoutOwnerEntity::getIcLayoutId, entity.getId()).list()
                    .stream().map(o -> BeanUtil.copyProperties(o, IcLayoutOwnerVO.class)).collect(Collectors.toList()));
            res.setCreators(creatorInService.lambdaQuery().eq(IcLayoutCreatorInEntity::getIcLayoutId, entity.getId()).list()
                    .stream().map(c -> BeanUtil.copyProperties(c, IcLayoutCreatorInVO.class)).collect(Collectors.toList()));
            return res;
        });
    }


    @Override
    public IcLayoutResponse getDetail(Long id) {
        IcLayoutEntity entity = this.getById(id);
        IcLayoutResponse res = new IcLayoutResponse();
        res.setMain(BeanUtil.copyProperties(entity, IcLayoutMainVO.class));
        res.setOwners(ownerService.lambdaQuery().eq(IcLayoutOwnerEntity::getIcLayoutId, id).list()
                .stream().map(o -> BeanUtil.copyProperties(o, IcLayoutOwnerVO.class)).collect(Collectors.toList()));
        res.setCreators(creatorInService.lambdaQuery().eq(IcLayoutCreatorInEntity::getIcLayoutId, id).list()
                .stream().map(c -> BeanUtil.copyProperties(c, IcLayoutCreatorInVO.class)).collect(Collectors.toList()));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeLayouts(List<Long> ids) {
        this.removeBatchByIds(ids);
        ownerService.removeByIcLayoutIds(ids);
        creatorInService.removeByIcLayoutIds(ids);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean replaceOwners(Long icLayoutId, List<IcLayoutOwnerVO> owners) {
        ownerService.removeByIcLayoutIds(List.of(icLayoutId));
        if (CollUtil.isNotEmpty(owners)) {
            List<IcLayoutOwnerEntity> entities = owners.stream()
                    .map(o -> {
                        IcLayoutOwnerEntity entity = BeanUtil.copyProperties(o, IcLayoutOwnerEntity.class);
                        entity.setIcLayoutId(icLayoutId);
                        return entity;
                    }).collect(Collectors.toList());
            ownerService.saveBatch(entities);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean replaceCreators(Long icLayoutId, List<IcLayoutCreatorInVO> creators) {
        creatorInService.removeByIcLayoutIds(List.of(icLayoutId));
        if (CollUtil.isNotEmpty(creators)) {
            List<IcLayoutCreatorInEntity> entities = creators.stream()
                    .map(c -> {
                        IcLayoutCreatorInEntity entity = BeanUtil.copyProperties(c, IcLayoutCreatorInEntity.class);
                        entity.setIcLayoutId(icLayoutId);
                        return entity;
                    }).collect(Collectors.toList());
            creatorInService.saveBatch(entities);
        }
        return Boolean.TRUE;
    }

}
