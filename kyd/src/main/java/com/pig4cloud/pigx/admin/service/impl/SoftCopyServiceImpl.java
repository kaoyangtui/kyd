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
import com.pig4cloud.pigx.admin.api.entity.SysFile;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.SoftCopyMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyResponse;
import com.pig4cloud.pigx.admin.dto.softCopy.SoftCopyUpdateRequest;
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
public class SoftCopyServiceImpl extends ServiceImpl<SoftCopyMapper, SoftCopyEntity> implements SoftCopyService {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProposal(SoftCopyCreateRequest request) {
        SoftCopyEntity entity = BeanUtil.copyProperties(request, SoftCopyEntity.class);
        entity.setCode(ParamResolver.getStr(SoftCopyResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        if (request.getAttachmentUrls() != null && !request.getAttachmentUrls().isEmpty()) {
            String fileUrl = StrUtil.join(";", request.getAttachmentUrls());
            entity.setAttachmentUrls(fileUrl);
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getAttachmentUrls().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        SoftCopyResponse.BIZ_CODE,
                        entity.getSoftName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
            fileService.batchCreate(fileCreateRequestList);
        }
        request.getCompleters().forEach(completer -> {
            if (completer.getCompleterLeader() == 1) {
                entity.setLeaderCode(completer.getCompleterNo());
                entity.setLeaderName(completer.getCompleterName());
            }
        });
        this.save(entity);
        Long softCopyId = entity.getId();
        if (ObjectUtil.isNull(softCopyId)) {
            throw new BizException("软著提案保存失败，未生成 ID");
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProposal(SoftCopyUpdateRequest request) {
        Long id = request.getId();
        SoftCopyEntity entity = BeanUtil.copyProperties(request, SoftCopyEntity.class);
        entity.setId(id);
        if (request.getAttachmentUrls() != null && !request.getAttachmentUrls().isEmpty()) {
            String fileUrl = StrUtil.join(";", request.getAttachmentUrls());
            entity.setAttachmentUrls(fileUrl);
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getAttachmentUrls().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        SoftCopyResponse.BIZ_CODE,
                        entity.getSoftName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
            fileService.batchCreate(fileCreateRequestList);
        }
        request.getCompleters().forEach(completer -> {
            if (completer.getCompleterLeader() == 1) {
                entity.setLeaderCode(completer.getCompleterNo());
                entity.setLeaderName(completer.getCompleterName());
            }
        });
        this.updateById(entity);

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    public IPage<SoftCopyResponse> pageResult(Page reqPage, SoftCopyPageRequest request) {
        LambdaQueryWrapper<SoftCopyEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(SoftCopyEntity::getId, request.getIds());
        } else {
            wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                    w.like(SoftCopyEntity::getCode, request.getKeyword())
                            .or().like(SoftCopyEntity::getSoftName, request.getKeyword()));

            wrapper.eq(StrUtil.isNotBlank(request.getTechField()), SoftCopyEntity::getTechField, request.getTechField());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), SoftCopyEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), SoftCopyEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), SoftCopyEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), SoftCopyEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), SoftCopyEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        IPage<SoftCopyEntity> page = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());

        return page.convert(entity -> {
            SoftCopyResponse response = BeanUtil.copyProperties(entity, SoftCopyResponse.class);
            response.setAttachmentUrls(StrUtil.split(entity.getAttachmentUrls(), ";"));
            return response;
        });
    }

    @SneakyThrows
    @Override
    public SoftCopyResponse getDetail(Long id) {
        SoftCopyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        SoftCopyResponse res = BeanUtil.copyProperties(entity, SoftCopyResponse.class);
        res.setAttachmentUrls(StrUtil.split(entity.getAttachmentUrls(), ";"));
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

}
