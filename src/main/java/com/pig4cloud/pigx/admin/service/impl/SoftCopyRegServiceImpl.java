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
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegUpdateRequest;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.mapper.SoftCopyRegMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.SoftCopyRegService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class SoftCopyRegServiceImpl extends ServiceImpl<SoftCopyRegMapper, SoftCopyRegEntity> implements SoftCopyRegService, FlowStatusUpdater {

    private final FileService fileService;
    private final OwnerService ownerService;
    private final CompleterService completerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean create(SoftCopyRegCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(SoftCopyRegUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    private void doSaveOrUpdate(SoftCopyRegCreateRequest request, boolean isCreate) {
        SoftCopyRegEntity entity = BeanUtil.copyProperties(request, SoftCopyRegEntity.class);

        if (isCreate) {
            entity.setCode(ParamResolver.getStr(SoftCopyRegResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        } else if (request instanceof SoftCopyRegUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
        }

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        SoftCopyRegResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.SOFT_COPY_CERT.getValue()
                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));
        }

        if (CollUtil.isNotEmpty(request.getCompleters())) {
            request.getCompleters().stream()
                    .filter(c -> ObjectUtil.equal(c.getCompleterLeader(), 1))
                    .findFirst()
                    .ifPresent(leader -> {
                        entity.setLeaderCode(leader.getCompleterNo());
                        entity.setLeaderName(leader.getCompleterName());
                    });
        }

        if (isCreate) {
            this.save(entity);
        } else {
            this.updateById(entity);
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
    }

    @SneakyThrows
    @Override
    public SoftCopyRegResponse getDetail(Long id) {
        SoftCopyRegEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        SoftCopyRegResponse res = convertToResponse(entity);
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    @Override
    public IPage<SoftCopyRegResponse> pageResult(Page page, SoftCopyRegPageRequest request) {
        LambdaQueryWrapper<SoftCopyRegEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(SoftCopyRegEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(SoftCopyRegEntity::getName, request.getKeyword())
                        .or()
                        .like(SoftCopyRegEntity::getRegNo, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), SoftCopyRegEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), SoftCopyRegEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), SoftCopyRegEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), SoftCopyRegEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), SoftCopyRegEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(SoftCopyRegEntity::getCreateTime);
        }

        IPage<SoftCopyRegEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    private SoftCopyRegResponse convertToResponse(SoftCopyRegEntity entity) {
        SoftCopyRegResponse res = BeanUtil.copyProperties(entity, SoftCopyRegResponse.class);
        res.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public String flowKey() {
        return SoftCopyRegResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(SoftCopyRegEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, SoftCopyRegEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), SoftCopyRegEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
