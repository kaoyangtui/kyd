package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyResponse;
import com.pig4cloud.pigx.admin.entity.EventMeetingApplyEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.EventMeetingApplyMapper;
import com.pig4cloud.pigx.admin.service.EventMeetingApplyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventMeetingApplyServiceImpl extends ServiceImpl<EventMeetingApplyMapper, EventMeetingApplyEntity> implements EventMeetingApplyService {

    @Override
    public IPage<EventMeetingApplyResponse> pageResult(Page page, EventMeetingApplyPageRequest request) {
        LambdaQueryWrapper<EventMeetingApplyEntity> wrapper = new LambdaQueryWrapper<>();
        if (request.getMeetingId() != null) {
            wrapper.eq(EventMeetingApplyEntity::getMeetingId, request.getMeetingId());
        }
        wrapper.like(StrUtil.isNotBlank(request.getName()), EventMeetingApplyEntity::getName, request.getName());
        wrapper.like(StrUtil.isNotBlank(request.getPhone()), EventMeetingApplyEntity::getPhone, request.getPhone());
        return this.page(page, wrapper).convert(entity -> BeanUtil.copyProperties(entity, EventMeetingApplyResponse.class));
    }

    @SneakyThrows
    @Override
    public EventMeetingApplyResponse getDetail(Long id) {
        EventMeetingApplyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return BeanUtil.copyProperties(entity, EventMeetingApplyResponse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createApply(EventMeetingApplyCreateRequest request) {
        EventMeetingApplyEntity entity = BeanUtil.copyProperties(request, EventMeetingApplyEntity.class);
        entity.setApplyTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeApplies(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }
}
