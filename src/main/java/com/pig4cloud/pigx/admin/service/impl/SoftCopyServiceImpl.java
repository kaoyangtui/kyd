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
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyUpdateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.mapper.SoftCopyMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.SoftCopyService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class SoftCopyServiceImpl extends ServiceImpl<SoftCopyMapper, SoftCopyEntity> implements SoftCopyService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean createProposal(SoftCopyCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateProposal(SoftCopyUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    private void doSaveOrUpdate(SoftCopyCreateRequest request, boolean isCreate) {
        SoftCopyEntity entity = BeanUtil.copyProperties(request, SoftCopyEntity.class);

        if (isCreate) {
            entity.setCode(ParamResolver.getStr(SoftCopyResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        } else if (request instanceof SoftCopyUpdateRequest updateReq) {
            entity.setId(updateReq.getId());
        }

        // 附件处理
        if (CollUtil.isNotEmpty(request.getAttachmentUrls())) {
            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getAttachmentUrls().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName, entity.getCode(),
                        SoftCopyResponse.BIZ_CODE, entity.getSoftName(),
                        FileBizTypeEnum.ATTACHMENT.getValue());
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
            entity.setAttachmentUrls(StrUtil.join(";", request.getAttachmentUrls()));
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
            this.save(entity);
            if (ObjectUtil.isNull(entity.getId())) {
                throw new BizException("软著提案保存失败，未生成 ID");
            }
        } else {
            this.updateById(entity);
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
    }

    @Override
    public IPage<SoftCopyResponse> pageResult(Page reqPage, SoftCopyPageRequest request) {
        LambdaQueryWrapper<SoftCopyEntity> wrapper = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(SoftCopyEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(SoftCopyEntity::getCode, request.getKeyword())
                                .or()
                                .like(SoftCopyEntity::getSoftName, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getTechField()), SoftCopyEntity::getTechField, request.getTechField());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), SoftCopyEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), SoftCopyEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), SoftCopyEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), SoftCopyEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), SoftCopyEntity::getCreateTime, request.getEndTime());
        }
        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(SoftCopyEntity::getCreateTime);
        }

        IPage<SoftCopyEntity> page = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());
        return page.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public SoftCopyResponse getDetail(Long id) {
        SoftCopyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        SoftCopyResponse res = convertToResponse(entity);
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    private SoftCopyResponse convertToResponse(SoftCopyEntity entity) {
        SoftCopyResponse res = BeanUtil.copyProperties(entity, SoftCopyResponse.class);
        res.setAttachmentUrls(StrUtil.split(entity.getAttachmentUrls(), ";"));
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public String flowKey() {
        return SoftCopyResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(SoftCopyEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, SoftCopyEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), SoftCopyEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
