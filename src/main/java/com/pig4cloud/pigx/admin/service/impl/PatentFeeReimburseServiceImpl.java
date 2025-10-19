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
import com.pig4cloud.pigx.admin.constants.PatentTypeEnum;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.patentFee.*;
import com.pig4cloud.pigx.admin.dto.result.ResultResponse;
import com.pig4cloud.pigx.admin.entity.PatentFeeItemEntity;
import com.pig4cloud.pigx.admin.entity.PatentFeeReimburseEntity;
import com.pig4cloud.pigx.admin.entity.ResearchProjectEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.PatentFeeReimburseMapper;
import com.pig4cloud.pigx.admin.mapper.ResearchProjectMapper;
import com.pig4cloud.pigx.admin.service.PatentFeeItemService;
import com.pig4cloud.pigx.admin.service.PatentFeeReimburseService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PatentFeeReimburseServiceImpl extends OrderCommonServiceImpl<PatentFeeReimburseMapper, PatentFeeReimburseEntity> implements PatentFeeReimburseService {

    private final PatentFeeItemService patentFeeItemService;
    private final ResearchProjectMapper researchProjectMapper;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatentFeeReimburseResponse createPatentFeeReimburse(PatentFeeReimburseCreateRequest request) {
        PatentFeeReimburseEntity entity = CopyUtil.copyProperties(request, PatentFeeReimburseEntity.class);
        String code = ParamResolver.getStr(PatentFeeReimburseResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
        entity.setCode(code);
        entity.setFlowKey(PatentFeeReimburseResponse.BIZ_CODE);
        entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
        entity.setIpCode(StrUtil.join(";", request.getIpCode()));
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("orderName", "【专利费用报销】" + entity.getTitle());
        super.saveOrUpdateOrder(params, entity);
        jsonFlowHandle.doStart(params, entity);
        // 明细通过 Service 批量插入
        if (CollUtil.isNotEmpty(request.getFeeItems())) {
            List<PatentFeeItemEntity> items = CollUtil.newArrayList();
            for (PatentFeeItemVO vo : request.getFeeItems()) {
                PatentFeeItemEntity item = CopyUtil.copyProperties(vo, PatentFeeItemEntity.class);
                item.setReimburseId(entity.getId());
                items.add(item);
            }
            patentFeeItemService.saveBatch(items);
        }
        return convertToResponse(entity);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePatentFeeReimburse(PatentFeeReimburseUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        PatentFeeReimburseEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        CopyUtil.copyProperties(request, entity);
        this.updateById(entity);

        // 删除原明细，重新插入，通过 Service 操作
        patentFeeItemService.remove(Wrappers.<PatentFeeItemEntity>lambdaQuery().eq(PatentFeeItemEntity::getReimburseId, entity.getId()));
        if (CollUtil.isNotEmpty(request.getFeeItems())) {
            List<PatentFeeItemEntity> items = CollUtil.newArrayList();
            for (PatentFeeItemVO vo : request.getFeeItems()) {
                PatentFeeItemEntity item = CopyUtil.copyProperties(vo, PatentFeeItemEntity.class);
                item.setReimburseId(entity.getId());
                items.add(item);
            }
            patentFeeItemService.saveBatch(items);
        }
        return true;
    }

    @Override
    public IPage<PatentFeeReimburseResponse> pageResult(Page page, PatentFeeReimbursePageRequest request) {
        LambdaQueryWrapper<PatentFeeReimburseEntity> wrapper = Wrappers.lambdaQuery();

        // 优先按ID查询
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PatentFeeReimburseEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getAppNumber()), PatentFeeReimburseEntity::getAppNumber, request.getAppNumber());
            wrapper.like(StrUtil.isNotBlank(request.getTitle()), PatentFeeReimburseEntity::getTitle, request.getTitle());
            wrapper.like(StrUtil.isNotBlank(request.getProposalCode()), PatentFeeReimburseEntity::getProposalCode, request.getProposalCode());
            wrapper.eq(StrUtil.isNotBlank(request.getPatType()), PatentFeeReimburseEntity::getPatType, request.getPatType());
            wrapper.like(StrUtil.isNotBlank(request.getCreateBy()), PatentFeeReimburseEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), PatentFeeReimburseEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getStartTime()), PatentFeeReimburseEntity::getCreateTime, request.getStartTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PatentFeeReimburseEntity::getCreateTime, request.getEndTime());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentFeeReimburseEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentFeeReimburseEntity::getCurrentNodeName, request.getCurrentNodeName());
        }

        // 分页ids/range优先
        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        // 默认排序
        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(PatentFeeReimburseEntity::getCreateTime);
        }

        // 查询并转换
        IPage<PatentFeeReimburseEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public PatentFeeReimburseResponse getDetail(Long id) {
        PatentFeeReimburseEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        PatentFeeReimburseResponse response = convertToResponse(entity);

        // 子表通过 Service 查询
        List<PatentFeeItemEntity> items = patentFeeItemService.list(Wrappers.<PatentFeeItemEntity>lambdaQuery().eq(PatentFeeItemEntity::getReimburseId, entity.getId()));
        if (CollUtil.isNotEmpty(items)) {
            response.setFeeItems(BeanUtil.copyToList(items, PatentFeeItemVO.class));
        }
        ResearchProjectEntity researchProject = researchProjectMapper.selectById(entity.getResearchProjectId());
        if (researchProject != null) {
            response.setResearchProjectType(researchProject.getProjectType());
            response.setResearchProjectName(researchProject.getProjectName());
        }
        return response;
    }


    @SneakyThrows
    @Override
    public Boolean removePatentFeeReimburse(IdListRequest request) {
        List<Long> ids = request.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }
        // 删除主表
        boolean result = this.removeBatchByIds(ids);
        // 删除子表
        patentFeeItemService.remove(Wrappers.<PatentFeeItemEntity>lambdaQuery().in(PatentFeeItemEntity::getReimburseId, ids));
        return result;
    }

    private PatentFeeReimburseResponse convertToResponse(PatentFeeReimburseEntity entity) {
        PatentFeeReimburseResponse response = CopyUtil.copyProperties(entity, PatentFeeReimburseResponse.class);
        response.setIpCode(StrUtil.split(entity.getIpCode(), ";"));
        response.setPatTypeName(PatentTypeEnum.getByCode(entity.getPatType()).getDescription());
        return response;
    }
}
