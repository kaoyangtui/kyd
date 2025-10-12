package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingCreateRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingPageRequest;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingResponse;
import com.pig4cloud.pigx.admin.dto.eventMeeting.EventMeetingUpdateRequest;
import com.pig4cloud.pigx.admin.entity.EventMeetingApplyEntity;
import com.pig4cloud.pigx.admin.entity.EventMeetingEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.EventMeetingMapper;
import com.pig4cloud.pigx.admin.service.EventMeetingApplyService;
import com.pig4cloud.pigx.admin.service.EventMeetingService;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventMeetingServiceImpl extends ServiceImpl<EventMeetingMapper, EventMeetingEntity> implements EventMeetingService {

    private final EventMeetingApplyService eventMeetingApplyService;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(EventMeetingEntity::getCreateTime);
        }

        IPage<EventMeetingEntity> entityPage = this.page(page, wrapper);
        // 这里把 userId 透传给转换方法，用于计算 signUpStatus
        Long userId = request.getUserId();
        return entityPage.convert(e -> convertToResponse(e, userId));
    }

    @SneakyThrows
    @Override
    public EventMeetingResponse getDetail(Long id) {
        EventMeetingEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        this.lambdaUpdate()
                .eq(EventMeetingEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        // 未提供 userId 的场景，仅按时间窗口判断可报名/不可报名（0/2）
        PigxUser user = SecurityUtils.getUser();
        if (user == null) {
            return convertToResponse(entity, null);
        }
        return convertToResponse(entity, user.getId());
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
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof EventMeetingUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }


    /** 新增重载：带 userId 以便计算 signUpStatus */
    private EventMeetingResponse convertToResponse(EventMeetingEntity entity, Long userId) {
        EventMeetingResponse response = new EventMeetingResponse();
        // 先做普通属性拷贝（相同类型字段）
        BeanUtil.copyProperties(entity, response);

        // 处理附件 -> List
        response.setFileUrl(StrUtil.isNotBlank(entity.getFileUrl())
                ? Arrays.stream(entity.getFileUrl().split(";"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList()
                : null);

        // 时间字段格式化为字符串
        response.setEventTime(entity.getEventTime());
        response.setSignUpStart(entity.getSignUpStart());
        response.setSignUpEnd(entity.getSignUpEnd());

        // 计算报名状态
        response.setSignUpStatus(calcSignUpStatus(entity, userId));

        return response;
    }

    /**
     * 报名状态：
     * 0 可报名：在报名窗口内，且（未提供 userId 或者）未报名
     * 1 已报名：在报名窗口内，且该 userId 已报名
     * 2 不可报名：不在报名窗口内（未开始或已结束）
     *
     * 窗口判定：start <= now <= end；start/end 允许为空（为空则不限制该端）
     */
    private int calcSignUpStatus(EventMeetingEntity e, Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate start = e.getSignUpStart();
        LocalDate end = e.getSignUpEnd();

        boolean afterStart = (start == null) || !now.isBefore(start); // now >= start
        boolean beforeEnd  = (end == null)   || !now.isAfter(end);     // now <= end
        boolean inWindow   = afterStart && beforeEnd;

        if (!inWindow) {
            return 2; // 不可报名
        }

        if (userId == null) {
            // 未提供用户，只能判定“在窗口期内”，视为可报名
            return 0;
        }

        long exists = eventMeetingApplyService.count(
                new LambdaQueryWrapper<EventMeetingApplyEntity>()
                        .eq(EventMeetingApplyEntity::getMeetingId, e.getId())
                        .eq(EventMeetingApplyEntity::getUserId, userId)
                        .last("limit 1")
        );

        return exists > 0 ? 1 : 0;
    }
}
