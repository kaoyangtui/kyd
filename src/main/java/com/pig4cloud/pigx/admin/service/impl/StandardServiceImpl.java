package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.dto.standard.StandardCreateRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardPageRequest;
import com.pig4cloud.pigx.admin.dto.standard.StandardResponse;
import com.pig4cloud.pigx.admin.dto.standard.StandardUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
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
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandardServiceImpl extends OrderCommonServiceImpl<StandardMapper, StandardEntity> implements StandardService, FlowStatusUpdater {

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
                        code,
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
            Map<String, Object> params = MapUtil.newHashMap();
            params.put("orderName", entity.getName());
            params.put("owners", request.getOwners());
            params.put("completers", request.getCompleters());
            super.saveOrUpdateOrder(params, entity);
            jsonFlowHandle.doStart(params, entity);
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
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), StandardEntity::getDeptId, request.getDeptId());
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
        String code = entity.getCode();
        StandardResponse res = convertToResponse(entity);
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, code).list());
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
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
                .set(dto.getFlowStatus() != null, StandardEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), StandardEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }

    /**
     * 标准：默认按“发布”统计（pub_date 落在区间内）
     * 参与人：t_completer 按 code 关联
     */
    @Override
    public List<PerfEventDTO> fetchStandardEvents(RuleEventEnum evt,
                                                  PerfRuleEntity rule,
                                                  LocalDate start,
                                                  LocalDate end) {

        final LocalDateTime begin = start == null ? null : start.atStartOfDay();
        final LocalDateTime finish = end == null ? null : end.plusDays(1).atStartOfDay().minusNanos(1);

        if (begin == null || finish == null) {
            return Collections.emptyList();
        }

        final String eventCode = evt.getCode();
        final String eventName = evt.getName();

        // 按发布时间过滤
        List<StandardEntity> list = this.lambdaQuery()
                .eq(StandardEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .ge(StandardEntity::getFlowStatusTime, begin)
                .le(StandardEntity::getFlowStatusTime, finish)
                .list();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 预取完成人，按 code 分组，避免 N+1
        List<String> codes = list.stream()
                .map(StandardEntity::getCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, List<CompleterEntity>> completerMap = codes.isEmpty()
                ? Collections.emptyMap()
                : completerService.lambdaQuery()
                .in(CompleterEntity::getCode, codes)
                .eq(CompleterEntity::getDelFlag, "0")
                .list()
                .stream()
                .collect(Collectors.groupingBy(CompleterEntity::getCode));

        // 组装事件
        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (StandardEntity r : list) {
            LocalDate d = r.getPubDate();
            if (d == null) continue;

            List<PerfParticipantDTO> participants = completerService.toParticipants(completerMap.get(r.getCode()));

            out.add(PerfEventDTO.builder()
                    // 用标准号 code 作为“件数”去重主键
                    .pid(r.getCode())
                    .eventTime(d.atStartOfDay())
                    .ipTypeCode(IpTypeEnum.STANDARD.getCode())
                    .ipTypeName(IpTypeEnum.STANDARD.getName())
                    .ruleEventCode(eventCode)
                    .ruleEventName(eventName)
                    .baseScore(rule.getScore())
                    .participants(participants)
                    .build());
        }
        return out;
    }

}
