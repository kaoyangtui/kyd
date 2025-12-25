package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.MaturityEnum;
import com.pig4cloud.pigx.admin.constants.TransWayEnum;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.result.*;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
import com.pig4cloud.pigx.admin.mapper.ResultMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.ResultService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-11 15:44:51
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ResultServiceImpl extends OrderCommonServiceImpl<ResultMapper, ResultEntity> implements ResultService, FlowStatusUpdater {

    private final FileService fileService;
    private final JsonFlowHandle jsonFlowHandle;
    private final CompleterService completerService;
    private final ResearchProjectMapper researchProjectMapper;

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultResponse createResult(ResultCreateRequest request) {
        return doSaveOrUpdate(request, true);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateResult(ResultUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    private ResultResponse doSaveOrUpdate(ResultCreateRequest request, boolean isCreate) throws BizException {
        ResultEntity entity = CopyUtil.copyProperties(request, ResultEntity.class);
        String code;
        if (!isCreate && request instanceof ResultUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(ResultResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        entity.setTechArea(StrUtil.join(";", request.getTechArea()));
        entity.setTransWay(StrUtil.join(";", request.getTransWay()));
        entity.setImgUrl(StrUtil.join(";", request.getImgUrl()));

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getFileUrl().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        code,
                        ResultResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                if (fileCreateRequest != null) {
                    fileCreateRequestList.add(fileCreateRequest);
                }
            });
            fileService.batchCreate(fileCreateRequestList);
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
            entity.setFlowKey(ResultResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            Map<String, Object> params = MapUtil.newHashMap();
            params.put("orderName", entity.getName());
            super.saveOrUpdateOrder(params, entity);
            jsonFlowHandle.doStart(params, entity);
        } else {
            this.updateById(entity);
        }
        completerService.replaceCompleters(code, request.getCompleters());
        return convertToResponse(entity);
    }

    @Override
    public IPage<ResultResponse> pageResult(Page reqPage, ResultPageRequest request, boolean isByScope) {
        LambdaQueryWrapper<ResultEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResultEntity::getId, request.getIds());
        } else {
            wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                    w.like(ResultEntity::getName, request.getKeyword())
                            .or().like(ResultEntity::getCode, request.getKeyword())
                            .or().like(ResultEntity::getTags, request.getKeyword()));
            wrapper.eq(StrUtil.isNotBlank(request.getLeaderCode()), ResultEntity::getLeaderCode, request.getLeaderCode());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), ResultEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(StrUtil.isNotBlank(request.getSubject()), ResultEntity::getSubject, request.getSubject());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), ResultEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResultEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), ResultEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), ResultEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ResultEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ResultEntity::getCreateTime, request.getEndTime());

            if (CollUtil.isNotEmpty(request.getTechArea())) {
                String regex = "(^|;)(" + CollUtil.join(request.getTechArea(), "|") + ")";
                wrapper.apply("tech_area REGEXP {0}", regex);
            }
            if (CollUtil.isNotEmpty(request.getTransWay())) {
                wrapper.in(ResultEntity::getTransWay, request.getTransWay());
            }
            if (CollUtil.isNotEmpty(request.getMaturity())) {
                wrapper.in(ResultEntity::getMaturity, request.getMaturity());
            }
            if (StrUtil.isNotBlank(request.getCompleterNo())) {
                wrapper.apply("exists (select 0 from t_completer " +
                        "where code = t_result.code and completer_no = {0})", request.getCompleterNo());
            }
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }
        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(ResultEntity::getCreateTime);
        }
        IPage<ResultEntity> entityPage;
        if (isByScope) {
            entityPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());
        } else {
            entityPage = baseMapper.selectPage(reqPage, wrapper);
        }

        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public ResultResponse getDetail(Long id) {
        ResultEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(ResultEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        String code = entity.getCode();
        ResultResponse response = convertToResponse(entity);
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
        return response;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeResult(IdListRequest request) {
        List<Long> ids = request.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }
        return this.removeBatchByIds(ids);
    }

    @Override
    public Boolean updateShelfStatus(ResultShelfRequest request) {
        return this.update(Wrappers.<ResultEntity>lambdaUpdate()
                .eq(ResultEntity::getId, request.getId())
                .set(ResultEntity::getShelfStatus, request.getShelfStatus())
                .set(ResultEntity::getShelfTime, LocalDateTime.now())
                .set(ObjectUtil.isNotNull(request.getTechArea()), ResultEntity::getTechArea, request.getTechArea())
                .set(CollUtil.isNotEmpty(request.getTags()), ResultEntity::getTags, StrUtil.join(";", request.getTags()))
                .set(CollUtil.isNotEmpty(request.getTransWay()), ResultEntity::getTransWay, StrUtil.join(";", request.getTransWay()))
                .set(ObjectUtil.isNotNull(request.getTransPrice()), ResultEntity::getTransPrice, request.getTransPrice())
        );
    }

    private ResultResponse convertToResponse(ResultEntity entity) {
        ResultResponse response = CopyUtil.copyProperties(entity, ResultResponse.class);
        response.setTechArea(StrUtil.split(entity.getTechArea(), ";"));
        // 传递方式：code 列表
        List<String> transWayCodes = StrUtil.split(entity.getTransWay(), ";");
        response.setTransWay(transWayCodes);

        // 传递方式：name 列表（用 TechTypeEnum 转）
        List<String> transWayNames = CollUtil.isEmpty(transWayCodes)
                ? CollUtil.newArrayList()
                : transWayCodes.stream()
                .map(Convert::toInt)
                .map(TransWayEnum::of)
                .map(TransWayEnum::getLabel)
                .collect(Collectors.toList());
        response.setTransWayName(transWayNames);

        response.setImgUrl(StrUtil.split(entity.getImgUrl(), ";"));
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        ResearchProjectEntity researchProject = researchProjectMapper.selectById(entity.getResearchProjectId());
        if (researchProject != null) {
            response.setResearchProjectType(researchProject.getProjectType());
            response.setResearchProjectName(researchProject.getProjectName());
        }
        response.setMaturityName(MaturityEnum.of(Integer.valueOf(entity.getMaturity())).getLabel());
        return response;
    }

    @Override
    public String flowKey() {
        return ResultResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(ResultEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, ResultEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, ResultEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), ResultEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }
}
