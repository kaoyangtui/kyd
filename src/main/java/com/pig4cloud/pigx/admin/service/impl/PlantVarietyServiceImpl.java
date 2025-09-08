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
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfEventDTO;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyCreateRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyPageRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyResponse;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.entity.PlantVarietyEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.PlantVarietyMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.PlantVarietyService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlantVarietyServiceImpl extends ServiceImpl<PlantVarietyMapper, PlantVarietyEntity> implements PlantVarietyService, FlowStatusUpdater {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(PlantVarietyCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PlantVarietyUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    private void doSaveOrUpdate(PlantVarietyCreateRequest request, boolean isCreate) {
        PlantVarietyEntity entity = BeanUtil.copyProperties(request, PlantVarietyEntity.class);
        String code;
        if (!isCreate && request instanceof PlantVarietyUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(PlantVarietyResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));

            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(file -> fileList.add(fileService.getFileCreateRequest(
                    file, code, PlantVarietyResponse.BIZ_CODE, entity.getName(), FileBizTypeEnum.PLANT_VARIETY_CERT.getValue()
            )));
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

        if (isCreate) {
            entity.setFlowKey(PlantVarietyResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            this.save(entity);
            //发起流程
            jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), entity.getName());
        } else {
            this.updateById(entity);
        }

        completerService.replaceCompleters(code, request.getCompleters());
        ownerService.replaceOwners(code, request.getOwners());
    }

    @SneakyThrows
    @Override
    public PlantVarietyResponse getDetail(Long id) {
        PlantVarietyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        String code = entity.getCode();
        PlantVarietyResponse response = convertToResponse(entity);
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
        response.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, code).list());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<PlantVarietyResponse> pageResult(Page<PlantVarietyEntity> page, PlantVarietyPageRequest request) {
        LambdaQueryWrapper<PlantVarietyEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PlantVarietyEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(PlantVarietyEntity::getName, request.getKeyword())
                        .or().like(PlantVarietyEntity::getRightNo, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PlantVarietyEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PlantVarietyEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PlantVarietyEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PlantVarietyEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PlantVarietyEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(PlantVarietyEntity::getCreateTime);
        }

        IPage<PlantVarietyEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(this::convertToResponse);
    }

    private PlantVarietyResponse convertToResponse(PlantVarietyEntity entity) {
        PlantVarietyResponse response = BeanUtil.copyProperties(entity, PlantVarietyResponse.class);
        response.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return PlantVarietyResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(PlantVarietyEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, PlantVarietyEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), PlantVarietyEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
    }

    /**
     * 植物新品种：仅按“发布”统计
     * 发布时间：authDate
     * 仅统计流程完结
     * 参与人：t_completer 按业务 code 关联
     */
    @Override
    public List<PerfEventDTO> fetchPlantVarietyEvents(RuleEventEnum evt,
                                                      PerfRuleEntity rule,
                                                      LocalDate start,
                                                      LocalDate end) {
        final String eventCode = rule.getRuleEventCode();
        final String eventName = rule.getRuleEventName();

        // 按授权时间过滤；必须流程完结
        List<PlantVarietyEntity> list = this.lambdaQuery()
                .eq(PlantVarietyEntity::getFlowStatus, FlowStatusEnum.FINISH.getStatus())
                .isNotNull(PlantVarietyEntity::getAuthDate)
                .ge(start != null, PlantVarietyEntity::getAuthDate, start)
                .le(end != null, PlantVarietyEntity::getAuthDate, end)
                .list();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 预取完成人，按业务 code 分组，避免 N+1
        List<String> codes = list.stream()
                .map(PlantVarietyEntity::getCode)
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

        // 组装事件：ipType 固定为 PLANT_VARIETY；件数按业务 code 去重
        List<PerfEventDTO> out = new ArrayList<>(list.size());
        for (PlantVarietyEntity r : list) {
            LocalDate d = r.getAuthDate();
            if (d == null) continue;

            List<PerfParticipantDTO> participants = completerService.toParticipants(completerMap.get(r.getCode()));
            out.add(PerfEventDTO.builder()
                    .pid(r.getCode())
                    .eventTime(d.atStartOfDay())
                    .ipTypeCode(IpTypeEnum.PLANT_VARIETY.getCode())
                    .ipTypeName(IpTypeEnum.PLANT_VARIETY.getName())
                    .ruleEventCode(eventCode)
                    .ruleEventName(eventName)
                    .baseScore(rule.getScore())
                    .participants(participants)
                    .build());
        }
        return out;
    }
}

