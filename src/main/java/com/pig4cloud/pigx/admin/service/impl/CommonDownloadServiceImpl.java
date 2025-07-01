package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadCreateRequest;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadPageRequest;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadResponse;
import com.pig4cloud.pigx.admin.dto.commonDownload.CommonDownloadUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CommonDownloadEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.CommonDownloadMapper;
import com.pig4cloud.pigx.admin.service.CommonDownloadService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommonDownloadServiceImpl extends ServiceImpl<CommonDownloadMapper, CommonDownloadEntity> implements CommonDownloadService {

    @Override
    public IPage<CommonDownloadResponse> pageResult(Page page, CommonDownloadPageRequest request) {
        LambdaQueryWrapper<CommonDownloadEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(CommonDownloadEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(CommonDownloadEntity::getFileName, request.getKeyword())
                        .or()
                        .like(CommonDownloadEntity::getContent, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), CommonDownloadEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), CommonDownloadEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), CommonDownloadEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), CommonDownloadEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<CommonDownloadEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public CommonDownloadResponse getDetail(Long id) {
        CommonDownloadEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(CommonDownloadCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(CommonDownloadUpdateRequest request) {
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(CommonDownloadCreateRequest request, boolean isCreate) {
        CommonDownloadEntity entity = BeanUtil.copyProperties(request, CommonDownloadEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof CommonDownloadUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private CommonDownloadResponse convertToResponse(CommonDownloadEntity entity) {
        CommonDownloadResponse response = BeanUtil.copyProperties(entity, CommonDownloadResponse.class);
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }
}

