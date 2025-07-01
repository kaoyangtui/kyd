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
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.demand.*;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.service.DemandService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandServiceImpl extends ServiceImpl<DemandMapper, DemandEntity> implements DemandService {

    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(DemandCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DemandUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public DemandResponse getDetail(Long id) {
        DemandEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<DemandResponse> pageResult(Page page, DemandPageRequest request) {
        LambdaQueryWrapper<DemandEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(DemandEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(DemandEntity::getName, request.getKeyword())
                                .or()
                                .like(DemandEntity::getTags, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getType()), DemandEntity::getType, request.getType());
            if (CollUtil.isNotEmpty(request.getField())) {
                wrapper.and(q -> request.getField().forEach(o -> q.or().like(DemandEntity::getField, o)));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), DemandEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getUserId()), DemandEntity::getUserId, request.getUserId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), DemandEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), DemandEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), DemandEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), DemandEntity::getCreateTime, request.getEndTime());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), DemandEntity::getShelfStatus, request.getShelfStatus());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<DemandEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    @Override
    public Boolean updateShelfStatus(DemandShelfRequest request) {
        return this.update(Wrappers.<DemandEntity>lambdaUpdate()
                .eq(DemandEntity::getId, request.getId())
                .set(DemandEntity::getShelfStatus, request.getShelfStatus())
                .set(DemandEntity::getShelfTime, LocalDateTime.now()));
    }

    private void doSaveOrUpdate(DemandCreateRequest request, boolean isCreate) {
        DemandEntity entity = BeanUtil.copyProperties(request, DemandEntity.class);
        entity.setTags(StrUtil.join(";", request.getTags()));

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getAttachFileUrl().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        DemandInResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                if (ObjectUtil.isNotNull(fileCreateRequest)) {
                    fileCreateRequestList.add(fileCreateRequest);
                }
            });
            fileService.batchCreate(fileCreateRequestList);
            entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        }

        if (!isCreate && request instanceof DemandUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            entity.setCode(ParamResolver.getStr(DemandResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
        }
    }

    private DemandResponse convertToResponse(DemandEntity entity) {
        DemandResponse response = BeanUtil.copyProperties(entity, DemandResponse.class);
        response.setTags(StrUtil.split(entity.getTags(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }
}
