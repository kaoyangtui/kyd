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
import com.pig4cloud.pigx.admin.dto.researchProject.*;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
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
        }
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }
        IPage<ResearchProjectEntity> resultPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resultPage.convert(entity -> BeanUtil.copyProperties(entity, ResearchProjectResponse.class));
    }

    @SneakyThrows
    @Override
    public ResearchProjectResponse getDetail(Long id) {
        ResearchProjectEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("科研项目不存在");
        }
        return BeanUtil.copyProperties(entity, ResearchProjectResponse.class);
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


}
