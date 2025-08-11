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
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.dto.researchProject.*;
import com.pig4cloud.pigx.admin.entity.PatentFeeReimburseEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
import com.pig4cloud.pigx.admin.service.PatentFeeReimburseService;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import com.pig4cloud.pigx.admin.service.ResearchProjectService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchProjectServiceImpl extends ServiceImpl<ResearchProjectMapper, ResearchProjectEntity> implements ResearchProjectService {

    private final ResultServiceImpl resultService;
    private final PatentProposalService patentProposalService;
    private final PatentFeeReimburseService patentFeeReimburseService;

    @Override
    public IPage<ResearchProjectResponse> pageResult(Page page, ResearchProjectPageRequest request) {
        LambdaQueryWrapper<ResearchProjectEntity> wrapper = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchProjectEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(ResearchProjectEntity::getProjectName, request.getKeyword())
                );
            }
            if (StrUtil.isNotBlank(request.getProjectType())) {
                wrapper.and(w ->
                        w.eq(ResearchProjectEntity::getProjectType, request.getProjectType())
                );
            }
            if (request.getStatus() != null) {
                wrapper.eq(ResearchProjectEntity::getStatus, request.getStatus());
            }
        }
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ResearchProjectEntity::getCreateTime);
        }

        IPage<ResearchProjectEntity> resultPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resultPage.convert(entity ->
                extracted(entity)
        );
    }

    @SneakyThrows
    @Override
    public ResearchProjectResponse getDetail(Long id) {
        ResearchProjectEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("科研项目不存在");
        }
        return extracted(entity);
    }

    private ResearchProjectResponse extracted(ResearchProjectEntity entity) {
        ResearchProjectResponse researchProjectResponse = BeanUtil.copyProperties(entity, ResearchProjectResponse.class);
        Long resultCount = resultService.lambdaQuery()
                .eq(ResultEntity::getResearchProjectId, entity.getId())
                .eq(ResultEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .count();
        researchProjectResponse.setResultCount(resultCount.intValue());
        Long proposalCount = patentProposalService.lambdaQuery()
                .eq(PatentProposalEntity::getResearchProjectId, entity.getId())
                .eq(PatentProposalEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .count();
        researchProjectResponse.setProposalCount(proposalCount.intValue());

        Long patentFeeReimburseCount = patentFeeReimburseService.lambdaQuery()
                .eq(PatentFeeReimburseEntity::getResearchProjectId, entity.getId())
                .eq(PatentFeeReimburseEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .count();
        researchProjectResponse.setPatentFeeReimburseCount(patentFeeReimburseCount.intValue());
        return researchProjectResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProject(ResearchProjectCreateRequest request) {
        ResearchProjectEntity entity = BeanUtil.copyProperties(request, ResearchProjectEntity.class);
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProject(ResearchProjectUpdateRequest request) {
        ResearchProjectEntity entity = BeanUtil.copyProperties(request, ResearchProjectEntity.class);
        entity.setId(request.getId());
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeProjects(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public List<ResearchProjectResponse> exportList(ResearchProjectPageRequest request) {
        LambdaQueryWrapper<ResearchProjectEntity> wrapper = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchProjectEntity::getId, request.getIds());
        }
        List<ResearchProjectEntity> list = this.list(wrapper);
        return BeanUtil.copyToList(list, ResearchProjectResponse.class);
    }

    @Override
    public List<String> projectTypeOptions(ProjectTypeSearchRequest request) {
        LambdaQueryWrapper<ResearchProjectEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.select(ResearchProjectEntity::getProjectType)
                .like(StrUtil.isNotBlank(request.getKeyword()), ResearchProjectEntity::getProjectType, request.getKeyword())
                .groupBy(ResearchProjectEntity::getProjectType);

        Page<ResearchProjectEntity> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ResearchProjectEntity> resultPage = this.page(page, wrapper);

        return resultPage.getRecords().stream()
                .map(ResearchProjectEntity::getProjectType)
                .distinct()
                .toList();
    }

    @Override
    public List<String> projectNameOptions(ProjectNameSearchRequest request) {
        LambdaQueryWrapper<ResearchProjectEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.select(ResearchProjectEntity::getProjectName)
                .like(StrUtil.isNotBlank(request.getKeyword()), ResearchProjectEntity::getProjectName, request.getKeyword())
                .groupBy(ResearchProjectEntity::getProjectName);

        Page<ResearchProjectEntity> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ResearchProjectEntity> resultPage = this.page(page, wrapper);

        return resultPage.getRecords().stream()
                .map(ResearchProjectEntity::getProjectName)
                .distinct()
                .toList();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean changeStatus(Long id, Integer status) {
        ResearchProjectEntity entity = this.getById(id);
        if (entity == null)
            throw new BizException("科研项目不存在");
        entity.setStatus(status);
        return this.updateById(entity);
    }

}
