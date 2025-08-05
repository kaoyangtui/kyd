package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.researchProject.*;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResearchProjectService extends IService<ResearchProjectEntity> {
    IPage<ResearchProjectResponse> pageResult(Page page, ResearchProjectPageRequest request);

    ResearchProjectResponse getDetail(Long id);

    Boolean createProject(ResearchProjectCreateRequest request);

    Boolean updateProject(ResearchProjectUpdateRequest request);

    Boolean removeProjects(List<Long> ids);

    List<ResearchProjectResponse> exportList(ResearchProjectPageRequest request);

    List<String> projectTypeOptions(ProjectTypeSearchRequest request);

    List<String> projectNameOptions(ProjectNameSearchRequest request);

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    Boolean changeStatus(Long id, Integer status);
}