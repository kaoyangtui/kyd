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
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentProposalMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.PatentProposalService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(PatentProposalEntity::getCode, request.getKeyword())
                        .or()
                        .like(PatentProposalEntity::getTitle, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getType()), PatentProposalEntity::getType, request.getType());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PatentProposalEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentProposalEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PatentProposalEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PatentProposalEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<PatentProposalEntity> resPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());
        return resPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public PatentProposalResponse getDetail(Long id) {
        PatentProposalEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        PatentProposalResponse res = convertToResponse(entity);
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean createProposal(PatentProposalCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateProposal(PatentProposalUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeProposals(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(PatentProposalCreateRequest request, boolean isCreate) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        if (isCreate) {
            entity.setCode(ParamResolver.getStr(PatentProposalResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        }

        List<FileCreateRequest> fileList = Lists.newArrayList();

        if (CollUtil.isNotEmpty(request.getClaimsFile())) {
            entity.setClaimsFile(StrUtil.join(";", request.getClaimsFile()));
            request.getClaimsFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.CLAIMS.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getDescriptionFile())) {
            entity.setDescriptionFile(StrUtil.join(";", request.getDescriptionFile()));
            request.getDescriptionFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.DESCRIPTION.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getAbstractFile())) {
            entity.setAbstractFile(StrUtil.join(";", request.getAbstractFile()));
            request.getAbstractFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.ABSTRACT.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getDescFigureFile())) {
            entity.setDescFigureFile(StrUtil.join(";", request.getDescFigureFile()));
        }

        if (CollUtil.isNotEmpty(request.getAbstractFigureFile())) {
            entity.setAbstractFigureFile(StrUtil.join(";", request.getAbstractFigureFile()));
        }

        if (!fileList.isEmpty()) {
            fileService.batchCreate(fileList);
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

        if (!isCreate && request instanceof PatentProposalUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
    }

    private PatentProposalResponse convertToResponse(PatentProposalEntity entity) {
        PatentProposalResponse response = BeanUtil.copyProperties(entity, PatentProposalResponse.class);
        response.setClaimsFile(StrUtil.split(entity.getClaimsFile(), ";"));
        response.setDescriptionFile(StrUtil.split(entity.getDescriptionFile(), ";"));
        response.setDescFigureFile(StrUtil.split(entity.getDescFigureFile(), ";"));
        response.setAbstractFile(StrUtil.split(entity.getAbstractFile(), ";"));
        response.setAbstractFigureFile(StrUtil.split(entity.getAbstractFigureFile(), ";"));
        return response;
    }
}
