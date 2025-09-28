package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentPreEval.PatentPreEvalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentPreEval.PatentPreEvalDetailRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalCreateRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalPageRequest;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalResponse;
import com.pig4cloud.pigx.admin.dto.patentProposal.PatentProposalUpdateRequest;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.PatentProposalMapper;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 专利提案表
 *
 * @author pigx
 * @date 2025-05-11 17:08:56
 */
@Service
@RequiredArgsConstructor
public class PatentProposalServiceImpl extends OrderCommonServiceImpl<PatentProposalMapper, PatentProposalEntity> implements PatentProposalService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;
    private final ResearchProjectMapper researchProjectMapper;
    private final JsonFlowHandle jsonFlowHandle;
    private final PatentPreEvalService patentPreEvalService;
    private final PatentFeeReimburseService patentFeeReimburseService;

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
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), PatentProposalEntity::getDeptId, request.getDeptId());
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
        String code = entity.getCode();
        PatentProposalResponse res = convertToResponse(entity);
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, code).list());
        //提交人不可以看申请前评估结果
        //if (!SecurityUtils.getUser().getId().equals(entity.getCreateUserId())) {
        PatentPreEvalDetailRequest patentPreEvalDetailRequest = new PatentPreEvalDetailRequest();
        patentPreEvalDetailRequest.setCode(code);
        res.setPatentPreEval(patentPreEvalService.detail(patentPreEvalDetailRequest));
        //}
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @SneakyThrows
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

    private void doSaveOrUpdate(PatentProposalCreateRequest request, boolean isCreate) throws BizException {
        PatentProposalEntity entity = BeanUtil.copyProperties(request, PatentProposalEntity.class);
        String code;
        if (!isCreate && request instanceof PatentProposalUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(PatentProposalResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }
        entity.setTechField(StrUtil.join(";", request.getTechField()));

        List<FileCreateRequest> fileList = Lists.newArrayList();

        if (CollUtil.isNotEmpty(request.getClaimsFile())) {
            entity.setClaimsFile(StrUtil.join(";", request.getClaimsFile()));
            request.getClaimsFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, code, PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_CLAIMS.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getDescriptionFile())) {
            entity.setDescriptionFile(StrUtil.join(";", request.getDescriptionFile()));
            request.getDescriptionFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, code, PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_DESCRIPTION.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getAbstractFile())) {
            entity.setAbstractFile(StrUtil.join(";", request.getAbstractFile()));
            request.getAbstractFile().forEach(file -> fileList.add(fileService.getFileCreateRequest(file, code, PatentProposalResponse.BIZ_CODE, entity.getTitle(), FileBizTypeEnum.PATENT_PROPOSAL_ABSTRACT.getValue())));
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
            Map<String, Object> params = MapUtil.newHashMap();
            params.put("orderName", entity.getTitle());
            super.saveOrUpdateOrder(params, entity);
            jsonFlowHandle.doStart(params, entity);
            PatentPreEvalCreateRequest preEvalRequest = new PatentPreEvalCreateRequest();
            preEvalRequest.setCode(entity.getCode());
            preEvalRequest.setTitle(entity.getTitle());
            preEvalRequest.setAppDate(LocalDate.now());
            request.getOwners().stream()
                    .filter(c -> c.getOwnerType() == 1)
                    .findFirst()
                    .ifPresent(leader -> {
                        preEvalRequest.setApplicant(leader.getOwnerName());
                    });
            preEvalRequest.setApplyDateRangeType("ANY");
            preEvalRequest.setAbstractText(entity.getAbstractText());
            preEvalRequest.setClaimText(entity.getClaimsText());
            preEvalRequest.setDescriptionText(entity.getDescriptionText());
            boolean bl = patentPreEvalService.create(preEvalRequest);
            if (!bl) {
                throw new BizException("预评估创建失败");
            }
        }

        completerService.replaceCompleters(code, request.getCompleters());
        ownerService.replaceOwners(code, request.getOwners());
    }

    private PatentProposalResponse convertToResponse(PatentProposalEntity entity) {
        PatentProposalResponse response = BeanUtil.copyProperties(entity, PatentProposalResponse.class);
        response.setTechField(StrUtil.split(entity.getTechField(), ";"));
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
        if (StrUtil.isNotBlank(entity.getPid())) {
            if (SecurityUtils.getUser().getUsername().equals(entity.getLeaderCode())) {
                boolean bl = patentFeeReimburseService.lambdaQuery()
                        .eq(PatentFeeReimburseEntity::getIpCode, entity.getPid())
                        .in(PatentFeeReimburseEntity::getFlowStatus,
                                FlowStatusEnum.RECALL.getStatus(),
                                FlowStatusEnum.INITIATE.getStatus(),
                                FlowStatusEnum.RUN.getStatus(),
                                FlowStatusEnum.FINISH.getStatus())
                        .exists();
                if (!bl) {
                    response.setIsReimburse(1);
                }
            }
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
                .set(dto.getFlowStatus() != null, PatentProposalEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), PatentProposalEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
