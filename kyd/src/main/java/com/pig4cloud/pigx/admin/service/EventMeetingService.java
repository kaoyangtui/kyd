package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingUpdateRequest;
import com.pig4cloud.pigx.admin.entity.EventMeetingEntity;

import java.util.List;

public interface EventMeetingService extends IService<EventMeetingEntity> {
    IPage<EventMeetingResponse> pageResult(Page page, EventMeetingPageRequest request);

    EventMeetingResponse getDetail(Long id);

    boolean createMeeting(EventMeetingCreateRequest request);

    boolean updateMeeting(EventMeetingUpdateRequest request);

    boolean removeMeetings(List<Long> ids);
}