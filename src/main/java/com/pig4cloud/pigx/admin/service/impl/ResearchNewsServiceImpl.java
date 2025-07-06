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
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsPageRequest;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsResponse;
import com.pig4cloud.pigx.admin.dto.researchNews.ResearchNewsUpdateRequest;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.entity.ResearchNewsEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchNewsMapper;
import com.pig4cloud.pigx.admin.service.ResearchNewsService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科研动态管理 ServiceImpl
 *
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class ResearchNewsServiceImpl extends ServiceImpl<ResearchNewsMapper, ResearchNewsEntity> implements ResearchNewsService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNews(ResearchNewsCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateNews(ResearchNewsUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    private void doSaveOrUpdate(ResearchNewsCreateRequest request, boolean isCreate) {
        ResearchNewsEntity entity = BeanUtil.copyProperties(request, ResearchNewsEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof ResearchNewsUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    @SneakyThrows
    @Override
    public ResearchNewsResponse getDetail(Long id) {
        ResearchNewsEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(ResearchNewsEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return convertToResponse(entity);
    }

    @Override
    public IPage<ResearchNewsResponse> pageResult(Page page, ResearchNewsPageRequest request) {
        LambdaQueryWrapper<ResearchNewsEntity> wrapper = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchNewsEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(ResearchNewsEntity::getTitle, request.getKeyword())
                        .or().like(ResearchNewsEntity::getContent, request.getKeyword())
                        .or().like(ResearchNewsEntity::getProvider, request.getKeyword()));
            }
            wrapper.like(StrUtil.isNotBlank(request.getCreateBy()), ResearchNewsEntity::getCreateBy, request.getCreateBy());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<ResearchNewsEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    private ResearchNewsResponse convertToResponse(ResearchNewsEntity entity) {
        ResearchNewsResponse response = BeanUtil.copyProperties(entity, ResearchNewsResponse.class);
        response.setFileUrl(StrUtil.isNotBlank(entity.getFileUrl()) ? StrUtil.split(entity.getFileUrl(), ";") : null);
        return response;
    }

    @Override
    public ExportFieldListResponse exportFields() {
        return ExportFieldHelper.buildExportFieldList(ResearchNewsResponse.BIZ_CODE, ResearchNewsResponse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeNews(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }
}


