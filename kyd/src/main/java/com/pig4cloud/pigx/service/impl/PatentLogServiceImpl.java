package com.pig4cloud.pigx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.constants.TopicConstants;
import com.pig4cloud.pigx.entity.PatentLogEntity;
import com.pig4cloud.pigx.mapper.PatentLogMapper;
import com.pig4cloud.pigx.service.PatentLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-15 17:10:58
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PatentLogServiceImpl extends ServiceImpl<PatentLogMapper, PatentLogEntity> implements PatentLogService {
    private final RocketMQClientTemplate rocketMQClientTemplate;

    @Override
    public Boolean updateStatus() {
        List<PatentLogEntity> list = this.lambdaQuery()
                .eq(PatentLogEntity::getStatus, 0)
                .list();
        list.forEach(item -> {
            item.setStatus(1);
            this.updateById(item);
            rocketMQClientTemplate.syncSendNormalMessage(
                    TopicConstants.TOPIC_PATENT,
                    item.getResponseBody());
        });
        return true;
    }
}