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
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutCreateRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutPageRequest;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutResponse;
import com.pig4cloud.pigx.admin.dto.icLayout.IcLayoutUpdateRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.IcLayoutMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.IcLayoutService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IcLayoutServiceImpl extends OrderCommonServiceImpl<IcLayoutMapper, IcLayoutEntity> implements IcLayoutService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IcLayoutCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IcLayoutUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public IcLayoutResponse getDetail(Long id) {
        IcLayoutEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        String code = entity.getCode();
        IcLayoutResponse response = convertToResponse(entity);
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
        response.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, code).list());
        return response;
    }

    @Override
    public IPage<IcLayoutResponse> pageResult(Page<IcLayoutEntity> page, IcLayoutPageRequest request) {
        LambdaQueryWrapper<IcLayoutEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IcLayoutEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w.like(IcLayoutEntity::getName, request.getKeyword())
                        .or().like(IcLayoutEntity::getRegNo, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), IcLayoutEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IcLayoutEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IcLayoutEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IcLayoutEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IcLayoutEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(IcLayoutEntity::getCreateTime);
        }

        IPage<IcLayoutEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(this::convertToResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(IcLayoutCreateRequest request, boolean isCreate) {
        IcLayoutEntity entity = CopyUtil.copyProperties(request, IcLayoutEntity.class);
        String code;
        if (!isCreate && request instanceof IcLayoutUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(IcLayoutResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }
        if (CollUtil.isNotEmpty(request.getCreatorOutName())) {
            entity.setCreatorOutName(StrUtil.join(";", request.getCreatorOutName()));
        }
        // 设置负责人
        if (CollUtil.isNotEmpty(request.getCompleters())) {
            request.getCompleters().stream()
                    .filter(c -> ObjectUtil.equal(c.getCompleterLeader(), 1))
                    .findFirst()
                    .ifPresent(leader -> {
                        entity.setLeaderCode(leader.getCompleterNo());
                        entity.setLeaderName(leader.getCompleterName());
                    });
        }

        // 设置附件
        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));

            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName, code,
                        IcLayoutResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.IC_LAYOUT_CERT.getValue()
                );
                fileList.add(file);
            });
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

        // 保存或更新
        if (!isCreate) {
            this.updateById(entity);
        } else {
            entity.setFlowKey(IcLayoutResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            Map<String, Object> params = MapUtil.newHashMap();
            params.put("orderName", entity.getName());
            super.saveOrUpdateOrder(params, entity);
            jsonFlowHandle.doStart(params, entity);
        }

        completerService.replaceCompleters(code, request.getCompleters());
        ownerService.replaceOwners(code, request.getOwners());
    }

    private IcLayoutResponse convertToResponse(IcLayoutEntity entity) {
        IcLayoutResponse response = CopyUtil.copyProperties(entity, IcLayoutResponse.class);
        response.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        response.setCreatorOutName(StrUtil.split(entity.getCreatorOutName(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return IcLayoutResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(IcLayoutEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, IcLayoutEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, IcLayoutEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), IcLayoutEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }


    // ===================== 集成电路布图 =====================
    @Override
    @SneakyThrows
    public List<PerfEventDTO> fetchIcLayoutEvents(RuleEventEnum evt,
                                                  PerfRuleEntity rule,
                                                  LocalDate start,
                                                  LocalDate end) {

        final LocalDateTime begin = start == null ? null : start.atStartOfDay();
        final LocalDateTime finish = end == null ? null : end.plusDays(1).atStartOfDay().minusNanos(1);

        if (begin == null || finish == null) {
            return Collections.emptyList();
        }

        // 事件编码/名称依赖上游配置
        final String eventCode = rule.getRuleEventCode();
        final String eventName = rule.getRuleEventName();


        List<IcLayoutEntity> list = this.lambdaQuery()
                .eq(IcLayoutEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .isNotNull(IcLayoutEntity::getApplyDate)
                .ge( IcLayoutEntity::getFlowStatusTime, begin)
                .le( IcLayoutEntity::getFlowStatusTime, finish)
                .list();

        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 预取完成人，按业务 code 分组，避免 N+1
        List<String> codes = list.stream()
                .map(IcLayoutEntity::getCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, List<CompleterEntity>> completerMap = codes.isEmpty()
                ? Collections.emptyMap()
                : completerService.lambdaQuery()
                .in(CompleterEntity::getCode, codes)
                .list()
                .stream()
                .collect(Collectors.groupingBy(CompleterEntity::getCode));

        // 组装事件：ipType 固定为 IC_LAYOUT；件数按业务 code 去重
        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (IcLayoutEntity r : list) {
            LocalDateTime eventTime = r.getFlowStatusTime();
            if (eventTime == null) {
                continue;
            }

            List<PerfParticipantDTO> participants = completerService.toParticipants(completerMap.get(r.getCode()));
            out.add(PerfEventDTO.builder()
                    .pid(r.getCode())
                    .eventTime(eventTime)
                    .ipTypeCode(IpTypeEnum.IC_LAYOUT.getCode())
                    .ipTypeName(IpTypeEnum.IC_LAYOUT.getName())
                    .ruleEventCode(eventCode)
                    .ruleEventName(eventName)
                    .baseScore(rule.getScore())
                    .participants(participants)
                    .build());
        }
        return out;
    }

}


