package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingApplyResponse;
import com.pig4cloud.pigx.admin.entity.EventMeetingApplyEntity;

import java.util.List;

public interface EventMeetingApplyService extends IService<EventMeetingApplyEntity> {
    IPage<EventMeetingApplyResponse> pageResult(Page page, EventMeetingApplyPageRequest request);

    EventMeetingApplyResponse getDetail(Long id);

    boolean createApply(EventMeetingApplyCreateRequest request);

    boolean removeApplies(List<Long> ids);
}