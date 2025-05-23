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
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentProposalMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专利提案表
 *
 * @author pigx
 * @date 2025-05-11 17:08:56
 */
@Service
@RequiredArgsConstructor
public class PatentProposalServiceImpl extends ServiceImpl<PatentProposalMapper, PatentProposalEntity> implements PatentProposalService {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;

    @Override
    public IPage<PatentProposalResponse> pageResult(Page reqPage, PatentProposalPageRequest request) {
        LambdaQueryWrapper<PatentProposalEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PatentProposalEntity::getId, request.getIds());
        } else {
            wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                    w.like(PatentProposalEntity::getCode, request.getKeyword())
                            .or().like(PatentProposalEntity::getTitle, request.getKeyword()));
            wrapper.eq(StrUtil.isNotBlank(request.getType()), PatentProposalEntity::getType, request.getType());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PatentProposalEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentProposalEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PatentProposalEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PatentProposalEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<PatentProposalEntity> resPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());

        return resPage.convert(entity -> BeanUtil.copyProperties(entity, PatentProposalResponse.class));
    }


    @SneakyThrows
    @Override
    public PatentProposalResponse getDetail(Long id) {
        PatentProposalEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        PatentProposalResponse res = BeanUtil.copyProperties(entity, PatentProposalResponse.class);
        res.setClaimsFile(StrUtil.split(entity.getClaimsFile(), ";"));
        res.setDescriptionFile(StrUtil.split(entity.getDescriptionFile(), ";"));
        res.setDescFigureFile(StrUtil.split(entity.getDescFigureFile(), ";"));
        res.setAbstractFigureFile(StrUtil.split(entity.getAbstractFigureFile(), ";"));
        res.setAbstractFile(StrUtil.split(entity.getAbstractFile(), ";"));
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    @Override
    public Boolean createProposal(PatentProposalCreateRequest request) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        entity.setCode(ParamResolver.getStr(PatentProposalResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
        if (request.getClaimsFile() != null && !request.getClaimsFile().isEmpty()) {
            entity.setClaimsFile(StrUtil.join(";", request.getClaimsFile()));
            request.getClaimsFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.CLAIMS.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getDescriptionFile() != null && !request.getDescriptionFile().isEmpty()) {
            entity.setDescriptionFile(StrUtil.join(";", request.getDescriptionFile()));
            request.getDescriptionFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.DESCRIPTION.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getDescFigureFile() != null && !request.getDescFigureFile().isEmpty()) {
            entity.setDescFigureFile(StrUtil.join(";", request.getDescFigureFile()));
        }
        if (request.getAbstractFile() != null && !request.getAbstractFile().isEmpty()) {
            entity.setAbstractFile(StrUtil.join(";", request.getAbstractFile()));
            request.getAbstractFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.ABSTRACT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getAbstractFigureFile() != null && !request.getAbstractFigureFile().isEmpty()) {
            entity.setAbstractFigureFile(StrUtil.join(";", request.getAbstractFigureFile()));
        }
        fileService.batchCreate(fileCreateRequestList);
        request.getCompleters().forEach(completer -> {
            if (completer.getCompleterLeader() == 1) {
                entity.setLeaderCode(completer.getCompleterNo());
                entity.setLeaderName(completer.getCompleterName());
            }
        });
        this.save(entity);

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateProposal(PatentProposalUpdateRequest request) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
        if (request.getClaimsFile() != null && !request.getClaimsFile().isEmpty()) {
            entity.setClaimsFile(StrUtil.join(";", request.getClaimsFile()));
            request.getClaimsFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.CLAIMS.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getDescriptionFile() != null && !request.getDescriptionFile().isEmpty()) {
            entity.setDescriptionFile(StrUtil.join(";", request.getDescriptionFile()));
            request.getDescriptionFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.DESCRIPTION.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getDescFigureFile() != null && !request.getDescFigureFile().isEmpty()) {
            entity.setDescFigureFile(StrUtil.join(";", request.getDescFigureFile()));
        }
        if (request.getAbstractFile() != null && !request.getAbstractFile().isEmpty()) {
            entity.setAbstractFile(StrUtil.join(";", request.getAbstractFile()));
            request.getAbstractFile().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PatentProposalResponse.BIZ_CODE,
                        entity.getTitle(),
                        FileBizTypeEnum.ABSTRACT.getValue()
                );
                fileCreateRequestList.add(fileCreateRequest);
            });
        }
        if (request.getAbstractFigureFile() != null && !request.getAbstractFigureFile().isEmpty()) {
            entity.setAbstractFigureFile(StrUtil.join(";", request.getAbstractFigureFile()));
        }
        fileService.batchCreate(fileCreateRequestList);
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
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

}