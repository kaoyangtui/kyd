package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.dto.demand.*;
import com.pig4cloud.pigx.admin.dto.demandIn.DemandInResponse;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DemandSignupEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DemandServiceImpl extends OrderCommonServiceImpl<DemandMapper, DemandEntity> implements DemandService, FlowStatusUpdater {

    private final FileService fileService;
    private final DemandReceiveService demandReceiveService;
    private final DemandSignupService demandSignupService;
    private final JsonFlowHandle jsonFlowHandle;
    private final SysUserService sysUserService;


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
        //List<DemandReceiveEntity> demandReceiveList = demandReceiveService.lambdaQuery()
        //        .eq(DemandReceiveEntity::getDemandId, entity.getId())
        //        .list();
        List<DemandSignupEntity> demandSignupList = demandSignupService.lambdaQuery()
                .eq(DemandSignupEntity::getDemandId, entity.getId())
                .list();
        this.lambdaUpdate()
                .eq(DemandEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        DemandResponse demandResponse = convertToResponse(entity);
        demandResponse.setDemandSignupList(demandSignupList);
        //demandResponse.setDemandReceiveList(demandReceiveList);
        return demandResponse;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    public IPage<DemandResponse> pageResult(Page page, DemandPageRequest request) {
        return pageResult(page, request, true);
    }

    @Override
    public IPage<DemandResponse> pageResult(Page page, DemandPageRequest request, boolean isByScope) {
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

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(DemandEntity::getCreateTime);
        }

        IPage<DemandEntity> entityPage;
        if (isByScope) {
            entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        } else {
            entityPage = baseMapper.selectPage(page, wrapper);
        }
        return entityPage.convert(this::convertToResponse);
    }

    @Override
    public Boolean updateShelfStatus(DemandShelfRequest request) {
        return this.update(Wrappers.<DemandEntity>lambdaUpdate()
                .in(DemandEntity::getId, request.getIds())
                .set(DemandEntity::getShelfStatus, request.getShelfStatus())
                .set(DemandEntity::getShelfTime, LocalDateTime.now()));
    }

    private void doSaveOrUpdate(DemandCreateRequest request, boolean isCreate) {
        DemandEntity entity = CopyUtil.copyProperties(request, DemandEntity.class);
        String code;
        if (!isCreate && request instanceof DemandUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(DemandResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        entity.setField(StrUtil.join(";", request.getField()));
        entity.setTags(StrUtil.join(";", request.getTags()));
        entity.setCompanyArea(StrUtil.join(";", request.getCompanyArea()));

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
                if (ObjectUtil.isNotNull(fileCreateRequest)) {
                    fileCreateRequestList.add(fileCreateRequest);
                }
            });
            fileService.batchCreate(fileCreateRequestList);
            entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        }

        if (!isCreate) {
            this.updateById(entity);
        } else {
            //需求分类，1 企业需求 2专项需求
            if (entity.getCategory() == 1) {
                if (null == entity.getUserId()) {
                    entity.setFlowKey(DemandResponse.BIZ_CODE);
                    entity.setFlowStatus(FlowStatusEnum.FINISH.getStatus());
                    entity.setFlowStatusTime(LocalDateTime.now());
                    entity.setCurrentNodeName("完成");
                    this.save(entity);
                } else {
                    entity.setFlowKey(DemandResponse.BIZ_CODE);
                    entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
                    Map<String, Object> params = MapUtil.newHashMap();
                    params.put("orderName", entity.getName());
                    super.saveOrUpdateOrder(params, entity);
                    jsonFlowHandle.doStart(params, entity);
                }
            } else if (entity.getCategory() == 2) {
                this.save(entity);
                List<SysUser> sysUserList = sysUserService.lambdaQuery().list();
                demandReceiveService.receive(entity, sysUserList.stream().map(SysUser::getUserId).toList());
            }
        }
    }



    private DemandResponse convertToResponse(DemandEntity entity) {
        DemandResponse response = CopyUtil.copyProperties(entity, DemandResponse.class);
        response.setTags(StrUtil.split(entity.getTags(), ";"));
        response.setField(StrUtil.split(entity.getField(), ";"));
        response.setCompanyArea(StrUtil.split(entity.getCompanyArea(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return DemandResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(DemandEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, DemandEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, DemandEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), DemandEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
