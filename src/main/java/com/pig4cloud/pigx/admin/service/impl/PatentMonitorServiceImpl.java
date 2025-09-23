package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pigx.admin.constants.MessageTemplateEnum;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorTransformEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorUserEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorMapper;
import com.pig4cloud.pigx.admin.service.MessageService;
import com.pig4cloud.pigx.admin.service.PatentMonitorService;
import com.pig4cloud.pigx.admin.service.PatentMonitorTransformService;
import com.pig4cloud.pigx.admin.service.PatentMonitorUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 专利监控表
 *
 * @author pigx
 * @date 2025-05-31 10:47:14
 */
@Slf4j
@Service
public class PatentMonitorServiceImpl extends ServiceImpl<PatentMonitorMapper, PatentMonitorEntity> implements PatentMonitorService {

    private final PatentMonitorUserService patentMonitorUserService;
    private final PatentMonitorTransformService patentMonitorTransformService;
    private final MessageService messageService;

    public PatentMonitorServiceImpl(@Lazy MessageService messageService,
                                    @Lazy PatentMonitorUserService patentMonitorUserService,
                                    @Lazy PatentMonitorTransformService patentMonitorTransformService) {
        this.patentMonitorUserService = patentMonitorUserService;
        this.patentMonitorTransformService = patentMonitorTransformService;
        this.messageService = messageService;
    }

    @SneakyThrows
    @Override
    public void create(String message) {
        // 1. 解析JSON和参数提取
        JSONObject jsonObject = JSONUtil.parseObj(message);
        JSONArray jsonArray = jsonObject.getJSONArray("legalList");
        if (CollUtil.isEmpty(jsonArray)) {
            throw new BizException("legalList不能为空");
        }
        JSONObject firstItem = jsonArray.getJSONObject(0);
        String prsDateStr = firstItem.getStr("prsDate");
        LocalDate prsDate = DateUtil.parse(prsDateStr, "yyyy.MM.dd").toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        String eventContent = firstItem.getStr("prsCode");

        // 2. 专利监控对象构建
        PatentMonitorEntity patentMonitor = JSONUtil.toBean(message, PatentMonitorEntity.class);
        patentMonitor.setId(null);
        patentMonitor.setTenantId(1L);
        patentMonitor.setEventTime(prsDate);
        patentMonitor.setEventContent(eventContent);

        // 3. 查重（按PID和eventTime）
        Long cnt = this.lambdaQuery()
                .eq(PatentMonitorEntity::getPid, patentMonitor.getPid())
                .eq(PatentMonitorEntity::getEventTime, prsDate)
                .count();
        if (cnt <= 0) {
            this.save(patentMonitor);
        }

        // 4. 生成消息和关系
        List<SysMessageEntity> sysMessageList = new ArrayList<>();
        List<SysMessageRelationEntity> sysMessageRelationList = new ArrayList<>();

        // 用户专利监控提醒
        handleMonitorUser(
                patentMonitor.getPid(),
                patentMonitor.getTitle(),
                eventContent,
                prsDate,
                sysMessageList,
                sysMessageRelationList);

        // 转化专利监控提醒
        handleMonitorTransform(
                patentMonitor.getPid(),
                patentMonitor.getTitle(),
                eventContent,
                prsDate,
                sysMessageList,
                sysMessageRelationList);

        // 5. 批量入库
        if (CollUtil.isNotEmpty(sysMessageList)) {
            messageService.saveMessage(sysMessageList);
        }
        if (CollUtil.isNotEmpty(sysMessageRelationList)) {
            messageService.saveMessageRelation(sysMessageRelationList);
        }
    }

    private void handleMonitorUser(String pid, String title, String eventContent, LocalDate prsDate,
                                   List<SysMessageEntity> sysMessageList,
                                   List<SysMessageRelationEntity> sysMessageRelationList) {
        List<PatentMonitorUserEntity> monitorUsers = patentMonitorUserService.lambdaQuery()
                .eq(PatentMonitorUserEntity::getPid, pid)
                .list();
        if (CollUtil.isEmpty(monitorUsers)) {
            return;
        }
        SysMessageEntity msg = messageService.buildMessage(
                MessageTemplateEnum.PATENT_MONITOR_STATUS_DETAIL,
                title, eventContent);
        sysMessageList.add(msg);
        monitorUsers.forEach(user -> {
            user.setEventTime(prsDate);
            SysMessageRelationEntity relation = new SysMessageRelationEntity();
            relation.setMsgId(msg.getId());
            relation.setUserId(user.getCreateUserId());
            relation.setReadFlag("0");
            sysMessageRelationList.add(relation);
        });
        patentMonitorUserService.updateBatchById(monitorUsers);
    }

    private void handleMonitorTransform(String pid, String title, String eventContent, LocalDate prsDate,
                                        List<SysMessageEntity> sysMessageList,
                                        List<SysMessageRelationEntity> sysMessageRelationList) {
        List<PatentMonitorTransformEntity> monitorTransforms = patentMonitorTransformService.lambdaQuery()
                .eq(PatentMonitorTransformEntity::getPid, pid)
                .list();
        if (CollUtil.isEmpty(monitorTransforms)) {
            return;
        }
        SysMessageEntity msg = messageService.buildMessage(
                MessageTemplateEnum.TRANSFER_MONITOR_CHANGE,
                title, eventContent);
        sysMessageList.add(msg);
        monitorTransforms.forEach(transform -> {
            transform.setEventTime(prsDate);
            SysMessageRelationEntity relation = new SysMessageRelationEntity();
            relation.setMsgId(msg.getId());
            relation.setUserId(transform.getCreateUserId());
            relation.setReadFlag("0");
            sysMessageRelationList.add(relation);
        });
        patentMonitorTransformService.updateBatchById(monitorTransforms);
    }


}