package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingUpdateRequest;
import com.pig4cloud.pigx.admin.entity.EventMeetingEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.EventMeetingMapper;
import com.pig4cloud.pigx.admin.service.EventMeetingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventMeetingServiceImpl extends ServiceImpl<EventMeetingMapper, EventMeetingEntity> implements EventMeetingService {

    @Override
    public IPage<EventMeetingResponse> pageResult(Page page, EventMeetingPageRequest request) {
        LambdaQueryWrapper<EventMeetingEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(EventMeetingEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(EventMeetingEntity::getName, request.getKeyword())
                        .or()
                        .like(EventMeetingEntity::getLocation, request.getKeyword())
                        .or()
                        .like(EventMeetingEntity::getContent, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), EventMeetingEntity::getCreateBy, request.getCreateBy());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), EventMeetingEntity::getEventTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), EventMeetingEntity::getEventTime, request.getEndTime());

            if (request.getUserId() != null) {
                wrapper.apply("exists (select 1 from t_event_meeting_apply " +
                        "where meeting_id = t_event_meeting.id and user_id = {0} and del_flag = '0')", request.getUserId());
            }
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<EventMeetingEntity> entityPage = this.page(page, wrapper);
        return entityPage.convert(this::convertToResponse);
    }

    @SneakyThrows
    @Override
    public EventMeetingResponse getDetail(Long id) {
        EventMeetingEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMeeting(EventMeetingCreateRequest request) {
        doSaveOrUpdate(request, true);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMeeting(EventMeetingUpdateRequest request) {
        doSaveOrUpdate(request, false);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMeetings(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    private void doSaveOrUpdate(EventMeetingCreateRequest request, boolean isCreate) {
        EventMeetingEntity entity = BeanUtil.copyProperties(request, EventMeetingEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            request.getFileUrl().replaceAll(fileName -> StrUtil.format(CommonConstants.FILE_GET_URL, fileName));
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof EventMeetingUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private EventMeetingResponse convertToResponse(EventMeetingEntity entity) {
        EventMeetingResponse response = BeanUtil.copyProperties(entity, EventMeetingResponse.class);
        response.setFileUrl(StrUtil.isNotBlank(entity.getFileUrl())
                ? StrUtil.split(entity.getFileUrl(), ";")
                : null);
        return response;
    }
}

