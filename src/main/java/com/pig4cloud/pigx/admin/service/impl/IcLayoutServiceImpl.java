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
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutCreateRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutPageRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutResponse;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutUpdateRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.mapper.IcLayoutMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.IcLayoutService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IcLayoutServiceImpl extends ServiceImpl<IcLayoutMapper, IcLayoutEntity> implements IcLayoutService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IcLayoutCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IcLayoutUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public IcLayoutResponse getDetail(Long id) {
        IcLayoutEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        IcLayoutResponse response = convertToResponse(entity);
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        response.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return response;
    }

    @Override
    public IPage<IcLayoutResponse> pageResult(Page<IcLayoutEntity> page, IcLayoutPageRequest request) {
        LambdaQueryWrapper<IcLayoutEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IcLayoutEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w.like(IcLayoutEntity::getName, request.getKeyword())
                        .or().like(IcLayoutEntity::getRegNo, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), IcLayoutEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IcLayoutEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IcLayoutEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IcLayoutEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IcLayoutEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(IcLayoutEntity::getCreateTime);
        }

        IPage<IcLayoutEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(this::convertToResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(IcLayoutCreateRequest request, boolean isCreate) {
        IcLayoutEntity entity = BeanUtil.copyProperties(request, IcLayoutEntity.class);

        if (isCreate) {
            entity.setCode(ParamResolver.getStr(IcLayoutResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        }

        // 设置负责人
        if (CollUtil.isNotEmpty(request.getCompleters())) {
            request.getCompleters().stream()
                    .filter(c -> ObjectUtil.equal(c.getCompleterLeader(), 1))
                    .findFirst()
                    .ifPresent(leader -> {
                        entity.setLeaderCode(leader.getCompleterNo());
                        entity.setLeaderName(leader.getCompleterName());
                    });
        }

        // 设置附件
        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));

            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName, entity.getCode(),
                        IcLayoutResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.IC_LAYOUT_CERT.getValue()
                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
        }

        // 保存或更新
        if (!isCreate && request instanceof IcLayoutUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            entity.setCode(ParamResolver.getStr(IcLayoutResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
    }

    private IcLayoutResponse convertToResponse(IcLayoutEntity entity) {
        IcLayoutResponse response = BeanUtil.copyProperties(entity, IcLayoutResponse.class);
        response.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return IcLayoutResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(IcLayoutEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, IcLayoutEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), IcLayoutEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}


