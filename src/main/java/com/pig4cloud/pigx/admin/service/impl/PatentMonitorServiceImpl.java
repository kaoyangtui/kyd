package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.constants.MessageTemplateEnum;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorUserEntity;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorMapper;
import com.pig4cloud.pigx.admin.service.PatentMonitorService;
import com.pig4cloud.pigx.admin.service.PatentMonitorUserService;
import com.pig4cloud.pigx.admin.service.SysMessageService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
public class PatentMonitorServiceImpl extends ServiceImpl<PatentMonitorMapper, PatentMonitorEntity> implements PatentMonitorService {

    @Lazy
    private final PatentMonitorUserService patentMonitorUserService;
    private final SysMessageService sysMessageService;
    @Override
    public void create(String message) {
        PatentMonitorEntity patentMonitor = JSONUtil.toBean(message, PatentMonitorEntity.class);
        patentMonitor.setId(null);
        patentMonitor.setTenantId(1L);
        JSONObject jsonObject = JSONUtil.parseObj(message);
        JSONArray jsonArray = jsonObject.getJSONArray("legalList");
        // 获取第一个项
        JSONObject firstItem = jsonArray.getJSONObject(0);
        String prsDateStr = firstItem.getStr("prsDate");
        LocalDate prsDate = DateUtil.parse(prsDateStr, "yyyy.MM.dd").toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        Long cnt = this.lambdaQuery()
                .eq(PatentMonitorEntity::getPid, patentMonitor.getPid())
                .eq(PatentMonitorEntity::getEventTime, prsDate)
                .count();
        if (cnt <= 0) {
            patentMonitor.setEventTime(prsDate);
            patentMonitor.setEventContent(firstItem.getStr("prsCode"));
            this.save(patentMonitor);
        }
        //处理用户专利监控
        List<PatentMonitorUserEntity> patentMonitorUserList = patentMonitorUserService.lambdaQuery()
                .eq(PatentMonitorUserEntity::getPid, patentMonitor.getPid())
                .list();
        if (CollUtil.isNotEmpty(patentMonitorUserList)) {
            List<SysMessageEntity> sysMessageList = new ArrayList<>();
            patentMonitorUserList.forEach(patentMonitorUser -> {
                patentMonitorUser.setEventTime(prsDate);
                SysMessageEntity messageEntity = new SysMessageEntity();
                messageEntity.setCategory("1");
                messageEntity.setTitle(MessageTemplateEnum.PATENT_MONITOR_STATUS_CHANGE.getDescription());
                messageEntity.setContent(StrUtil.format(MessageTemplateEnum.PATENT_MONITOR_STATUS_CHANGE.getTemplate(),
                        patentMonitor.getTitle(),
                        patentMonitor.getEventContent()));
                messageEntity.setSendFlag("1");
                messageEntity.setAllFlag("0");
                messageEntity.setSort((int) System.currentTimeMillis() / 1000);
                sysMessageList.add(messageEntity);
            });
            if (CollUtil.isNotEmpty(sysMessageList)) {
                sysMessageService.saveBatch(sysMessageList);
            }
            patentMonitorUserService.updateBatchById(patentMonitorUserList);
        }
    }
}