package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyCompleterService;
import com.pig4cloud.pigx.admin.service.SoftCopyOwnerService;
import com.pig4cloud.pigx.admin.service.SoftCopyService;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyUpdateRequest;
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
public class SoftCopyServiceImpl extends ServiceImpl<SoftCopyMapper, SoftCopyEntity> implements SoftCopyService {

    private final SoftCopyCompleterService completerService;
    private final SoftCopyOwnerService ownerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProposal(SoftCopyCreateRequest request) {
        SoftCopyEntity entity = BeanUtil.copyProperties(request, SoftCopyEntity.class);
        entity.setCode("RT" + IdUtil.getSnowflakeNextIdStr());
        this.save(entity);
        Long softCopyId = entity.getId();
        if (ObjectUtil.isNull(softCopyId)) {
            throw new IllegalStateException("软著提案保存失败，未生成 ID");
        }

        List<SoftCopyCompleterEntity> completerEntities = BeanUtil.copyToList(request.getCompleters(), SoftCopyCompleterEntity.class);
        List<SoftCopyOwnerEntity> ownerEntities = BeanUtil.copyToList(request.getOwners(), SoftCopyOwnerEntity.class);

        completerService.replaceCompleters(softCopyId, completerEntities);
        ownerService.replaceOwners(softCopyId, ownerEntities);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProposal(SoftCopyUpdateRequest request) {
        Long id = request.getId();
        SoftCopyEntity entity = BeanUtil.copyProperties(request, SoftCopyEntity.class);
        entity.setId(id);
        this.updateById(entity);

        List<SoftCopyCompleterEntity> completerEntities = BeanUtil.copyToList(request.getCompleters(), SoftCopyCompleterEntity.class);
        List<SoftCopyOwnerEntity> ownerEntities = BeanUtil.copyToList(request.getOwners(), SoftCopyOwnerEntity.class);

        completerService.replaceCompleters(id, completerEntities);
        ownerService.replaceOwners(id, ownerEntities);
        return Boolean.TRUE;
    }

    @Override
    public IPage<SoftCopyResponse> pageResult(Page reqPage, SoftCopyPageRequest request) {
        LambdaQueryWrapper<SoftCopyEntity> wrapper = Wrappers.lambdaQuery();

        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                w.like(SoftCopyEntity::getCode, request.getKeyword())
                        .or().like(SoftCopyEntity::getSoftName, request.getKeyword()));

        wrapper.eq(StrUtil.isNotBlank(request.getTechField()), SoftCopyEntity::getTechField, request.getTechField());
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), SoftCopyEntity::getDeptId, request.getDeptId());
        wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), SoftCopyEntity::getFlowStatus, request.getFlowStatus());
        wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), SoftCopyEntity::getCurrentNodeName, request.getCurrentNodeName());
        wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), SoftCopyEntity::getCreateTime, request.getBeginTime());
        wrapper.le(StrUtil.isNotBlank(request.getEndTime()), SoftCopyEntity::getCreateTime, request.getEndTime());

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<SoftCopyEntity> page = baseMapper.selectPageByScope(
                reqPage,
                wrapper,
                DataScope.of()
        );

        return page.convert(entity -> BeanUtil.copyProperties(entity, SoftCopyResponse.class));
    }

    @Override
    public SoftCopyResponse getDetail(Long id) {
        SoftCopyEntity entity = this.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("数据不存在");
        }
        SoftCopyResponse res = BeanUtil.copyProperties(entity, SoftCopyResponse.class);
        res.setCompleters(completerService.lambdaQuery().eq(SoftCopyCompleterEntity::getSoftCopyId, id).list());
        res.setOwners(ownerService.lambdaQuery().eq(SoftCopyOwnerEntity::getSoftCopyId, id).list());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

}
