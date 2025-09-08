package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInCreateRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInPageRequest;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInUpdateRequest;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegUpdateRequest;
import com.pig4cloud.pigx.admin.entity.DemandInEntity;
import com.pig4cloud.pigx.admin.entity.PatentClaimEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.DemandInMapper;
import com.pig4cloud.pigx.admin.service.DemandInService;
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
public class DemandInServiceImpl extends ServiceImpl<DemandInMapper, DemandInEntity> implements DemandInService, FlowStatusUpdater {

    private final FileService fileService;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(DemandInCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DemandInUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public DemandInResponse getDetail(Long id) {
        DemandInEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(DemandInEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        return convertToResponse(entity);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    public IPage<DemandInResponse> pageResult(Page page, DemandInPageRequest request) {
        return pageResult(page, request, true);
    }
    @Override
    public IPage<DemandInResponse> pageResult(Page page, DemandInPageRequest request, boolean isByScope) {
        LambdaQueryWrapper<DemandInEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(DemandInEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), DemandInEntity::getName, request.getKeyword());
            wrapper.in(CollUtil.isNotEmpty(request.getType()), DemandInEntity::getType, request.getType());
            if (CollUtil.isNotEmpty(request.getField())) {
                wrapper.and(q -> request.getField().forEach(o -> q.or().like(DemandInEntity::getField, o)));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), DemandInEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), DemandInEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), DemandInEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), DemandInEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), DemandInEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), DemandInEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(DemandInEntity::getCreateTime);
        }

        Page<DemandInEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateShelfStatus(Long id, Integer shelfStatus) {
        if (ObjectUtil.isNull(id)) {
            throw new BizException("ID不能为空");
        }
        DemandInEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setShelfStatus(shelfStatus);
        return this.updateById(entity);
    }

    private void doSaveOrUpdate(DemandInCreateRequest request, boolean isCreate) {
        DemandInEntity entity = BeanUtil.copyProperties(request, DemandInEntity.class);
        String code;
        if (!isCreate && request instanceof DemandInUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(DemandInResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        entity.setTags(StrUtil.join(";", request.getTags()));

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getAttachFileUrl().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        code,
                        DemandInResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
            fileService.batchCreate(fileCreateRequestList);

            entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        }

        if (!isCreate) {
            this.updateById(entity);
        } else {
            entity.setFlowKey(DemandInResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
            //发起流程
            jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), entity.getName());
        }
    }

    private DemandInResponse convertToResponse(DemandInEntity entity) {
        DemandInResponse response = BeanUtil.copyProperties(entity, DemandInResponse.class);
        response.setTags(StrUtil.split(entity.getTags(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return DemandInResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(DemandInEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, DemandInEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, DemandInEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), DemandInEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}

