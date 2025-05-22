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
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.mapper.PatentProposalMapper;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 专利提案表
 *
 * @author pigx
 * @date 2025-05-11 17:08:56
 */
@Service
public class PatentProposalServiceImpl extends ServiceImpl<PatentProposalMapper, PatentProposalEntity> implements PatentProposalService {
    @Override
    public IPage<PatentProposalResponse> pageResult(Page reqPage, PatentProposalPageRequest request) {
        LambdaQueryWrapper<PatentProposalEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PatentProposalEntity::getId, request.getIds());
        } else {
            wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                    w.like(PatentProposalEntity::getCode, request.getKeyword())
                            .or().like(PatentProposalEntity::getTitle, request.getKeyword()));
            wrapper.eq(StrUtil.isNotBlank(request.getType()), PatentProposalEntity::getType, request.getType());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PatentProposalEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentProposalEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PatentProposalEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PatentProposalEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<PatentProposalEntity> resPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());

        return resPage.convert(entity -> BeanUtil.copyProperties(entity, PatentProposalResponse.class));
    }


    @Override
    public PatentProposalResponse getDetail(Long id) {
        PatentProposalEntity entity = this.getById(id);
        return Optional.ofNullable(entity)
                .map(e -> BeanUtil.copyProperties(e, PatentProposalResponse.class))
                .orElse(null);
    }

    @Override
    public Boolean createProposal(PatentProposalCreateRequest request) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        entity.setCode("ZT" + IdUtil.getSnowflakeNextIdStr());
        return this.save(entity);
    }

    @Override
    public Boolean updateProposal(PatentProposalUpdateRequest request) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        return this.updateById(entity);
    }

    @Override
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public List<PatentProposalResponse> exportList(PatentProposalPageRequest request) {
        LambdaQueryWrapper<PatentProposalEntity> wrapper = Wrappers.lambdaQuery();

        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                w.like(PatentProposalEntity::getCode, request.getKeyword())
                        .or().like(PatentProposalEntity::getTitle, request.getKeyword()));

        wrapper.eq(StrUtil.isNotBlank(request.getType()), PatentProposalEntity::getType, request.getType());
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PatentProposalEntity::getDeptId, request.getDeptId());
        wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentProposalEntity::getFlowStatus, request.getFlowStatus());
        wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, request.getCurrentNodeName());

        List<PatentProposalEntity> list = this.list(wrapper);
        return list.stream()
                .map(e -> BeanUtil.copyProperties(e, PatentProposalResponse.class))
                .collect(Collectors.toList());
    }
}