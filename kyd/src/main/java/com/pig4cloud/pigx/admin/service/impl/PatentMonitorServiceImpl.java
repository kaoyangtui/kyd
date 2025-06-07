package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorMapper;
import com.pig4cloud.pigx.admin.service.PatentMonitorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 专利监控表
 *
 * @author pigx
 * @date 2025-05-31 10:47:14
 */
@Service
public class PatentMonitorServiceImpl extends ServiceImpl<PatentMonitorMapper, PatentMonitorEntity> implements PatentMonitorService {
    @Override
    public void create(String message) {
        PatentMonitorEntity patentMonitor = JSONUtil.toBean(message, PatentMonitorEntity.class);
        patentMonitor.setId(null);
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
    }
}