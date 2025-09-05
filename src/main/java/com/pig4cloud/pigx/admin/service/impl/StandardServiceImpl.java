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
import com.pig4cloud.pigx.admin.dto.demand.DemandResponse;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.dto.standard.StandardCreateRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardPageRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardResponse;
import com.pig4cloud.pigx.admin.dto.standard.StandardUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyEntity;
import com.pig4cloud.pigx.admin.entity.StandardEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.StandardMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.StandardService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StandardServiceImpl extends ServiceImpl<StandardMapper, StandardEntity> implements StandardService, FlowStatusUpdater {

    private final FileService fileService;
    private final OwnerService ownerService;
    private final CompleterService completerService;
    private final JsonFlowHandle jsonFlowHandle;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(StandardCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(StandardUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    private void doSaveOrUpdate(StandardCreateRequest request, boolean isCreate) {
        StandardEntity entity = BeanUtil.copyProperties(request, StandardEntity.class);
        String code;
        if (!isCreate && request instanceof StandardUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(StandardResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        // 附件处理
        if (CollUtil.isNotEmpty(request.getFileUrls())) {
            List<FileCreateRequest> fileCreateRequestList = CollUtil.newArrayList();
            for (String fileName : request.getFileUrls()) {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        StandardResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            }
            fileService.batchCreate(fileCreateRequestList);
            entity.setFileUrl(StrUtil.join(";", request.getFileUrls()));
        }

        // 负责人处理
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
            entity.setFlowKey(StandardResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
            if (ObjectUtil.isNull(entity.getId())) {
                throw new BizException("标准信息保存失败，未生成 ID");
            }
            //发起流程
            jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), entity.getName());
        } else {
            this.updateById(entity);
        }

        ownerService.replaceOwners(code, request.getOwners());
        completerService.replaceCompleters(code, request.getCompleters());
    }

    @Override
    public IPage<StandardResponse> pageResult(Page page, StandardPageRequest request) {
        LambdaQueryWrapper<StandardEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(StandardEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(StandardEntity::getCode, request.getKeyword())
                                .or()
                                .like(StandardEntity::getName, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), StandardEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), StandardEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), StandardEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), StandardEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), StandardEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(StandardEntity::getCreateTime);
        }

        IPage<StandardEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public StandardResponse getDetail(Long id) {
        StandardEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("标准信息不存在");
        }
        StandardResponse res = convertToResponse(entity);
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        return res;
    }

    private StandardResponse convertToResponse(StandardEntity entity) {
        StandardResponse response = BeanUtil.copyProperties(entity, StandardResponse.class);
        response.setFileUrls(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public String flowKey() {
        return StandardResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(StandardEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, StandardEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), StandardEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
