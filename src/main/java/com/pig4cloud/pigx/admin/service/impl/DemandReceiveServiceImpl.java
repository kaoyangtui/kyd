package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pigx.admin.constants.MessageTemplateEnum;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;
import com.pig4cloud.pigx.admin.mapper.DemandMapper;
import com.pig4cloud.pigx.admin.mapper.DemandReceiveMapper;
import com.pig4cloud.pigx.admin.service.DemandReceiveService;
import com.pig4cloud.pigx.admin.service.MessageService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 企业需求接收表
 *
 * @author pigx
 * @date 2025-07-29 12:33:46
 */
@RequiredArgsConstructor
@Service
public class DemandReceiveServiceImpl extends ServiceImpl<DemandReceiveMapper, DemandReceiveEntity> implements DemandReceiveService {

    private final DemandMapper demandMapper;
    private final MessageService messageService;

    @Override
    public Boolean receive(DemandReceiveRequest request) {
        DemandEntity demand = demandMapper.selectById(request.getDemandId());
        receive(demand, Collections.singletonList(request.getUserId()));
        return Boolean.TRUE;
    }

    @Override
    public IPage<DemandReceiveResponse> pageResult(Page reqPage, DemandReceivePageRequest request) {
        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            long size = request.getEndNo() - request.getStartNo() + 1;
            reqPage.setSize(Math.max(size, 1));
            reqPage.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }
        Long userId = SecurityUtils.getUser().getId();
        return baseMapper.selectReceivePage(reqPage, request, userId);
    }

    /**
     * 发送数据到所有的教职工用户
     *
     * @param entity
     */
    @Override
    public void receive(DemandEntity entity, List<Long> userIds) {
        List<DemandReceiveEntity> demandReceiveEntityList = Lists.newArrayList();
        List<SysMessageRelationEntity> sysMessageRelationList = Lists.newArrayList();
        SysMessageEntity msg = messageService.buildMessage(
                MessageTemplateEnum.DEMAND_RECEIVE,
                entity.getName());
        messageService.saveMessage(Collections.singletonList(msg));
        for (Long userId : userIds) {
            DemandReceiveEntity demandReceiveEntity = new DemandReceiveEntity();
            demandReceiveEntity.setDemandId(entity.getId());
            demandReceiveEntity.setReceiveUserId(userId);
            demandReceiveEntity.setReadFlag(0);
            demandReceiveEntityList.add(demandReceiveEntity);
            SysMessageRelationEntity relation = new SysMessageRelationEntity();
            relation.setMsgId(msg.getId());
            relation.setUserId(userId);
            relation.setReadFlag("0");
            sysMessageRelationList.add(relation);
        }
        if (CollUtil.isNotEmpty(demandReceiveEntityList)) {
            this.saveBatch(demandReceiveEntityList);
        }
        if (CollUtil.isNotEmpty(sysMessageRelationList)) {
            messageService.saveMessageRelation(sysMessageRelationList);
        }
    }

}