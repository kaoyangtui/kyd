package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.result.*;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResultMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.ResultService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-11 15:44:51
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, ResultEntity> implements ResultService {

    private final FileService fileService;
    private final RunFlowService runFlowService;
    private final CompleterService completerService;

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
        ResultEntity entity;
        if (isCreate) {
            entity = BeanUtil.copyProperties(request, ResultEntity.class);
            entity.setCode(ParamResolver.getStr(ResultResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        } else {
            ResultEntity existing = this.getById(((ResultUpdateRequest) request).getId());
            if (existing == null) {
                throw new BizException("数据不存在");
            }
            entity = existing;
            BeanUtil.copyProperties(request, entity, CopyOptions.create().ignoreNullValue());
        }

        entity.setTechArea(StrUtil.join(";", request.getTechArea()));
        entity.setTransWay(StrUtil.join(";", request.getTransWay()));
        entity.setImgUrl(StrUtil.join(";", request.getImgUrl()));

        if (CollUtil.isNotEmpty(request.getFileNames())) {
            List<FileCreateRequest> fileCreateRequestList = Lists.newArrayList();
            request.getFileNames().forEach(fileName -> {
                FileCreateRequest fileCreateRequest = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        ResultResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.ATTACHMENT.getValue()
                );
                if (fileCreateRequest != null) {
                    fileCreateRequestList.add(fileCreateRequest);
                }
            });
            fileService.batchCreate(fileCreateRequestList);
            entity.setFileUrl(StrUtil.join(";", request.getFileNames()));
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
            this.save(entity);
            //发起流程
            Map<String, Object> order = MapUtil.newHashMap();
            //	 * @param order  工单数据（必须字段: id, flowInstId, flowKey或defFlowId）
            order.put("id", entity.getId());
            order.put("flowInstId", entity.getFlowInstId());
            order.put("flowKey", entity.getFlowKey());
            Map<String, Object> params = MapUtil.newHashMap();
            params.put("flowInstId", entity.getFlowInstId());
            params.put("code", entity.getCode());
            params.put("flowKey", entity.getFlowKey());
            params.put("orderName", entity.getName());
            Boolean bl = runFlowService.startFlow(order, params);
            if (!bl) {
                throw new BizException("流程启动失败");
            }
        } else {
            this.updateById(entity);
        }

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        return convertToResponse(entity);
    }

    @Override
    public IPage<ResultResponse> pageResult(Page reqPage, ResultPageRequest request) {
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
                wrapper.apply("techArea REGEXP {0}", regex);
            }
            if (CollUtil.isNotEmpty(request.getTransWay())) {
                wrapper.in(ResultEntity::getTransWay, request.getTransWay());
            }
            if (CollUtil.isNotEmpty(request.getMaturity())) {
                wrapper.in(ResultEntity::getMaturity, request.getMaturity());
            }
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        IPage<ResultEntity> entityPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());
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
        ResultResponse response = convertToResponse(entity);
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
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
        ResultResponse response = BeanUtil.copyProperties(entity, ResultResponse.class);
        response.setTechArea(StrUtil.split(entity.getTechArea(), ";"));
        response.setTransWay(StrUtil.split(entity.getTransWay(), ";"));
        response.setImgUrl(StrUtil.split(entity.getImgUrl(), ";"));
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }
}
