package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignPageRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignUpdateRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.entity.IpAssignEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.IpAssignMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.IpAssignService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IpAssignServiceImpl extends ServiceImpl<IpAssignMapper, IpAssignEntity> implements IpAssignService, FlowStatusUpdater {

    private final FileService fileService;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IpAssignCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IpAssignUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("更新失败：ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public IpAssignResponse getDetail(Long id) {
        IpAssignEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("赋权信息不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public Page<IpAssignResponse> pageResult(Page page, IpAssignPageRequest request) {
        LambdaQueryWrapper<IpAssignEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IpAssignEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(IpAssignEntity::getCode, request.getKeyword())
                                .or()
                                .like(IpAssignEntity::getAssignToCode, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IpAssignEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IpAssignEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), IpAssignEntity::getDeptId, request.getCreateByDept());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IpAssignEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IpAssignEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(IpAssignEntity::getCreateTime);
        }

        IPage<IpAssignEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        Page<IpAssignResponse> resultPage = new Page<>(page.getCurrent(), page.getSize(), entityPage.getTotal());

        List<IpAssignResponse> records = entityPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        resultPage.setRecords(records);
        return resultPage;
    }

    private void doSaveOrUpdate(IpAssignCreateRequest request, boolean isCreate) {
        IpAssignEntity entity = BeanUtil.copyProperties(request, IpAssignEntity.class);
        String code;
        if (!isCreate && request instanceof IpAssignUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(IpAssignResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        if (CollUtil.isNotEmpty(request.getProofFileUrl())) {
            entity.setProofFileUrl(StrUtil.join(";", request.getProofFileUrl()));
        }

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        }

        List<FileCreateRequest> fileList = CollUtil.newArrayList();

        if (CollUtil.isNotEmpty(request.getProofFileUrl())) {
            request.getProofFileUrl().forEach(fileName -> {
                fileList.add(fileService.getFileCreateRequest(
                        fileName,
                        code,
                        IpAssignResponse.BIZ_CODE,
                        FileBizTypeEnum.IP_ASSIGN_PROOF.getBizName(),
                        FileBizTypeEnum.IP_ASSIGN_PROOF.getValue()));
            });
        }

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            request.getAttachFileUrl().forEach(fileName -> {
                fileList.add(fileService.getFileCreateRequest(
                        fileName,
                        code,
                        IpAssignResponse.BIZ_CODE,
                        FileBizTypeEnum.IP_ASSIGN_ATTACH.getBizName(),
                        FileBizTypeEnum.IP_ASSIGN_ATTACH.getValue()));
            });
        }

        if (!fileList.isEmpty()) {
            fileService.batchCreate(fileList);
        }

        if (!isCreate) {
            this.updateById(entity);
        } else {
            entity.setFlowKey(IpAssignResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
            //发起流程
            jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), "赋权");
        }
    }

    private IpAssignResponse convertToResponse(IpAssignEntity entity) {
        IpAssignResponse response = BeanUtil.copyProperties(entity, IpAssignResponse.class);
        response.setProofFileUrl(StrUtil.split(entity.getProofFileUrl(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return IpAssignResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(IpAssignEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, IpAssignEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, IpAssignEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), IpAssignEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }

    // ===================== 赋权 =====================

    /**
     * 知识产权赋权事件
     * 条件：流程完结、未删除，创建时间在 [start, end]
     * 参与人：默认仅“被赋权人”
     */
    @Override
    public List<PerfEventDTO> fetchEmpowerEvents(RuleEventEnum evt,
                                                 PerfRuleEntity rule,
                                                 LocalDate start,
                                                 LocalDate end) {
        final LocalDateTime begin = start == null ? null : start.atStartOfDay();
        final LocalDateTime finish = end == null ? null : end.plusDays(1).atStartOfDay().minusNanos(1);

        if (begin == null || finish == null) {
            return Collections.emptyList();
        }

        List<IpAssignEntity> list = this.lambdaQuery()
                .eq(IpAssignEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .ge(IpAssignEntity::getFlowStatusTime, begin)
                .le(IpAssignEntity::getFlowStatusTime, finish)
                .list();

        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        final String eventCode = evt.getCode();
        final String eventName = evt.getName();

        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (IpAssignEntity r : list) {
            LocalDateTime eventTime = r.getCreateTime();
            if (eventTime == null) {
                continue;
            }

            // 参与人：仅被赋权人
            List<PerfParticipantDTO> participants = new ArrayList<>(1);
            if (r.getAssignToCode() != null && !r.getAssignToCode().isEmpty()) {
                participants.add(PerfParticipantDTO.builder()
                        .userId(null)
                        .userCode(r.getAssignToCode())
                        .userName(r.getAssignToName())
                        .deptId(null)       // 若能由学工号反查部门，可在此补齐
                        .deptName(null)
                        .priority(null)
                        .isLeader(1)        // 被赋权人视为该事件主负责人
                        .build());
            }

            // 如需把发起人或“完成人”也计入，请在此处追加 participants
            // 例如：根据 r.getCode() 从 t_completer 取人并追加
            // participants.addAll(toParticipants(completerMap.get(r.getCode())));

            out.add(PerfEventDTO.builder()
                    .pid(r.getCode())                         // 业务编码作为去重主键
                    .eventTime(eventTime)
                    .ipTypeCode(IpTypeEnum.EMPOWER.getCode()) // 赋权类型
                    .ipTypeName(IpTypeEnum.EMPOWER.getName())
                    .ruleEventCode(eventCode)
                    .ruleEventName(eventName)
                    .baseScore(rule.getScore())
                    .participants(participants)
                    .build());
        }
        return out;
    }

}

