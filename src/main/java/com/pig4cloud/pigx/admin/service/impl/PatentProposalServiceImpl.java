package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
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
import com.pig4cloud.pigx.admin.dto.patentEvaluation.PatentEvaluationCommitRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.PatentProposalMapper;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
import com.pig4cloud.pigx.admin.service.*;
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
public class PatentProposalServiceImpl extends ServiceImpl<PatentProposalMapper, PatentProposalEntity> implements PatentProposalService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;
    private final ResearchProjectMapper researchProjectMapper;
    private final PatentEvaluationService PatentEvaluationService;
    private final JsonFlowHandle jsonFlowHandle;

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

        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(PatentProposalEntity::getCreateTime);
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
        return (Boolean) this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(PatentProposalCreateRequest request, boolean isCreate) {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        String code;
        if (!isCreate && request instanceof PatentProposalUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(PatentProposalResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        List<FileCreateRequest> fileList = Lists.newArrayList();

        if (CollUtil.isNotEmpty(request.getClaimsFile())) {
            entity.setClaimsFile(StrUtil.join(";", request.getClaimsFile()));
            request.getClaimsFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_CLAIMS.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getDescriptionFile())) {
            entity.setDescriptionFile(StrUtil.join(";", request.getDescriptionFile()));
            request.getDescriptionFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_DESCRIPTION.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getAbstractFile())) {
            entity.setAbstractFile(StrUtil.join(";", request.getAbstractFile()));
            request.getAbstractFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, entity.getCode(), PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_ABSTRACT.getValue())));
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
                    .filter(c -> c.getCompleterLeader() == 1)
                    .findFirst()
                    .ifPresent(leader -> {
                        entity.setLeaderCode(leader.getCompleterNo());
                        entity.setLeaderName(leader.getCompleterName());
                    });
        }

        if (!isCreate) {
            this.updateById(entity);
        } else {
            entity.setFlowKey(PatentProposalResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
            //发起流程
            jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), entity.getTitle());
            //提交申请前评估
            PatentEvaluationCommitRequest pecRequest = new PatentEvaluationCommitRequest();
            pecRequest.setSysBizId(entity.getId().toString());
            pecRequest.setTitle(entity.getTitle());
            request.getOwners().stream()
                    .filter(c -> c.getOwnerType() == 1)
                    .findFirst()
                    .ifPresent(leader -> {
                        pecRequest.setApplicant(leader.getOwnerName());
                    });
            pecRequest.setAppDate(DateUtil.format(DateUtil.date(), "yyyyMMdd"));
            pecRequest.setAbstractText(entity.getAbstractText());
            pecRequest.setClaimText(entity.getClaimsText());
            pecRequest.setDescriptionText(entity.getDescriptionText());
            PatentEvaluationService.commit(pecRequest);
        }

        completerService.replaceCompleters(code, request.getCompleters());
        ownerService.replaceOwners(code, request.getOwners());
    }

    private PatentProposalResponse convertToResponse(PatentProposalEntity entity) {
        PatentProposalResponse response = BeanUtil.copyProperties(entity, PatentProposalResponse.class);
        response.setClaimsFile(StrUtil.split(entity.getClaimsFile(), ";"));
        response.setDescriptionFile(StrUtil.split(entity.getDescriptionFile(), ";"));
        response.setDescFigureFile(StrUtil.split(entity.getDescFigureFile(), ";"));
        response.setAbstractFile(StrUtil.split(entity.getAbstractFile(), ";"));
        response.setAbstractFigureFile(StrUtil.split(entity.getAbstractFigureFile(), ";"));
        ResearchProjectEntity researchProject = researchProjectMapper.selectById(entity.getResearchProjectId());
        if (researchProject != null) {
            response.setResearchProjectType(researchProject.getProjectType());
            response.setResearchProjectName(researchProject.getProjectName());
        }
        return response;
    }

    @Override
    public String flowKey() {
        return PatentProposalResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(PatentProposalEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, PatentProposalEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
