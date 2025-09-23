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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipTransform.*;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoSimpleVO;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.entity.IpTransformPlanEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorTransformEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.IpTransformMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IpTransformServiceImpl extends OrderCommonServiceImpl<IpTransformMapper, IpTransformEntity> implements IpTransformService, FlowStatusUpdater {

    private final FileService fileService;
    private final IpTransformPlanService ipTransformPlanService;
    private final PatentInfoService patentInfoService;
    private final JsonFlowHandle jsonFlowHandle;
    private final PatentMonitorTransformService patentMonitorTransformService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IpTransformCreateRequest request) {
        doSaveOrUpdate(request, true);
        return true;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IpTransformUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return true;
    }

    @SneakyThrows
    @Override
    public IpTransformResponse getDetail(Long id) {
        IpTransformEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        //this.lambdaUpdate()
        //        .eq(IpTransformEntity::getId, id)
        //        .setSql("view_count = ifnull(view_count,0) + 1")
        //        .update();
        IpTransformResponse response = convertToResponse(entity);
        List<IpTransformPlanEntity> plans = ipTransformPlanService.lambdaQuery()
                .eq(IpTransformPlanEntity::getTransformId, entity.getId()).list();
        if (CollUtil.isNotEmpty(plans)) {
            response.setIpTransformPlanVOS(BeanUtil.copyToList(plans, IpTransformPlanVO.class));
        }
        List<PatentInfoEntity> patentInfos = patentInfoService.lambdaQuery()
                .in(PatentInfoEntity::getPid, response.getIpCode()).list();
        if (CollUtil.isNotEmpty(patentInfos)) {
            response.setPatentInfoSimpleVOS(BeanUtil.copyToList(patentInfos, PatentInfoSimpleVO.class));
        }
        return response;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<IpTransformResponse> pageResult(Page page, IpTransformPageRequest request) {
        LambdaQueryWrapper<IpTransformEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IpTransformEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(IpTransformEntity::getName, request.getKeyword())
                                .or()
                                .like(IpTransformEntity::getCode, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IpTransformEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IpTransformEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), IpTransformEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), IpTransformEntity::getDeptId, request.getCreateByDept());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IpTransformEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IpTransformEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(IpTransformEntity::getCreateTime);
        }

        IPage<IpTransformEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    private void doSaveOrUpdate(IpTransformCreateRequest request, boolean isCreate) {
        IpTransformEntity entity = BeanUtil.copyProperties(request, IpTransformEntity.class);
        String code;
        if (!isCreate && request instanceof IpTransformUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            code = this.getById(updateRequest.getId()).getCode();
        } else {
            code = ParamResolver.getStr(IpTransformResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
            entity.setCode(code);
        }

        entity.setIpCode(StrUtil.join(";", request.getIpCode()));

        List<FileCreateRequest> fileList = Lists.newArrayList();

        if (CollUtil.isNotEmpty(request.getConsentFileUrl())) {
            entity.setConsentFileUrl(StrUtil.join(";", request.getConsentFileUrl()));
            request.getConsentFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_CONSENT.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getPromiseFileUrl())) {
            entity.setPromiseFileUrl(StrUtil.join(";", request.getPromiseFileUrl()));
            request.getPromiseFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_PROMISE.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getRecordFileUrl())) {
            entity.setRecordFileUrl(StrUtil.join(";", request.getRecordFileUrl()));
            request.getRecordFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_RECORD.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getContractFileUrl())) {
            entity.setContractFileUrl(StrUtil.join(";", request.getContractFileUrl()));
            request.getContractFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_CONTRACT.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getRewardApplyFileUrl())) {
            entity.setRewardApplyFileUrl(StrUtil.join(";", request.getRewardApplyFileUrl()));
            request.getRewardApplyFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_REWARD_APPLY.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getAllocationPlanFileUrl())) {
            entity.setAllocationPlanFileUrl(StrUtil.join(";", request.getAllocationPlanFileUrl()));
            request.getAllocationPlanFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName,
                            code,
                            IpTransformResponse.BIZ_CODE,
                            entity.getName(),
                            FileBizTypeEnum.IP_TRANSFORM_ALLOCATION_PLAN.getValue())));
        }

        if (!fileList.isEmpty()) {
            fileService.batchCreate(fileList);
        }

        if (!isCreate) {
            this.updateById(entity);
        } else {
            entity.setFlowKey(IpTransformResponse.BIZ_CODE);
            entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
            // 设置流程参数
            Map<String, Object> params = MapUtil.newHashMap();
            super.saveOrUpdateOrder(params, entity);
            jsonFlowHandle.doStart(params, entity);
        }

        if (CollUtil.isNotEmpty(request.getIpTransformPlanVOS())) {
            List<IpTransformPlanEntity> planList = BeanUtil.copyToList(request.getIpTransformPlanVOS(), IpTransformPlanEntity.class);
            planList.forEach(plan -> {
                plan.setTransformId(entity.getId());
                plan.setTransformCode(code);
            });
            ipTransformPlanService.saveBatch(planList);
        }
    }

    private IpTransformResponse convertToResponse(IpTransformEntity entity) {
        IpTransformResponse response = BeanUtil.copyProperties(entity, IpTransformResponse.class);
        response.setIpCode(StrUtil.split(entity.getIpCode(), ";"));
        response.setConsentFileUrl(StrUtil.split(entity.getConsentFileUrl(), ";"));
        response.setPromiseFileUrl(StrUtil.split(entity.getPromiseFileUrl(), ";"));
        response.setContractFileUrl(StrUtil.split(entity.getContractFileUrl(), ";"));
        response.setRewardApplyFileUrl(StrUtil.split(entity.getRewardApplyFileUrl(), ";"));
        response.setAllocationPlanFileUrl(StrUtil.split(entity.getAllocationPlanFileUrl(), ";"));
        response.setRecordFileUrl(StrUtil.split(entity.getRecordFileUrl(), ";"));
        return response;
    }

    @Override
    public String flowKey() {
        return IpTransformResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(IpTransformEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, IpTransformEntity::getFlowStatus, dto.getFlowStatus())
                .set(dto.getFlowStatus() != null, IpTransformEntity::getFlowStatusTime, LocalDateTime.now())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), IpTransformEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
        if (dto.getFlowStatus() != null && dto.getFlowStatus().equals(FlowStatusEnum.FINISH.getStatus())) {
            IpTransformEntity ipTransform = this.lambdaQuery()
                    .eq(IpTransformEntity::getFlowInstId, dto.getFlowInstId())
                    .one();
            String pid = ipTransform.getIpCode();
            String code = ipTransform.getCode();
            String name = ipTransform.getName();
            //流程完毕创建转化专利监控
            PatentMonitorTransformEntity entity = patentMonitorTransformService.lambdaQuery()
                    .eq(PatentMonitorTransformEntity::getPid, pid)
                    .eq(PatentMonitorTransformEntity::getCode, code)
                    .last("limit 1")
                    .one();
            if (entity == null) {
                PatentInfoEntity patentInfo = patentInfoService.lambdaQuery()
                        .eq(PatentInfoEntity::getPid, pid).one();
                if (patentInfo == null) {
                    return;
                }
                entity = new PatentMonitorTransformEntity();
                entity.setPid(pid);
                entity.setAppNumber(patentInfo.getAppNumber());
                entity.setTitle(patentInfo.getTitle());
                entity.setCode(code);
                entity.setName(name);
                entity.setPatType(patentInfo.getPatType());
                entity.setSignDate(ipTransform.getContractSignTime());
                entity.setExpireDate(ipTransform.getContractExpireTime());
                entity.setDeptId(ipTransform.getDeptId());
                entity.setDeptName(ipTransform.getDeptName());
                entity.setCreateBy(ipTransform.getCreateBy());
                entity.setCreateUserId(ipTransform.getCreateUserId());
                patentMonitorTransformService.save(entity);
            }
        }

    }
}
