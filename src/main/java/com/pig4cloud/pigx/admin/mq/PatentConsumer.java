package com.pig4cloud.pigx.admin.mq;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.entity.PatentDetailEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.service.PatentDetailService;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.service.PatentInventorService;
import com.pig4cloud.pigx.admin.service.PatentMonitorService;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = TopicConstants.TOPIC_PATENT,
        consumerGroup = TopicConstants.CONSUMER_GROUP_PATENT + "_" + TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}",
        selectorExpression = TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}"
)
public class PatentConsumer implements RocketMQListener<String> {

    private final PatentInfoService patentInfoService;
    private final PatentDetailService patentDetailService;
    private final PatentInventorService patentInventorService;
    private final PatentMonitorService patentMonitorService;

    @Override
    public void onMessage(String message) {
        try {
            if (StrUtil.isBlank(message)) {
                log.warn("接收到的消息为空，跳过处理");
                return;
            }

            log.info("========== 【专利消息处理开始】==========");
            log.info("接收到消息内容: {}", message);

            // 主表信息保存
            PatentInfoEntity patentInfo = JSONUtil.toBean(message, PatentInfoEntity.class);
            log.info("解析主表信息完成，专利ID: {}", patentInfo.getPid());
            patentInfoService.create(patentInfo, message);
            log.info("主表信息保存完成");

            // 详情信息保存
            PatentDetailEntity patentDetail = JSONUtil.toBean(message, PatentDetailEntity.class);
            patentDetail.setId(null);
            patentDetail.setCitationForwardCountry(CodeUtils.formatCodes(patentDetail.getCitationForwardCountry()));
            log.info("解析详情信息完成");
            patentDetailService.save(patentDetail);
            log.info("详情信息保存完成");

            // 发明人处理
            List<String> inventorNames = StrUtil.split(patentInfo.getInventorName(), ';');
            log.info("发明人分割完成，共 {} 人", inventorNames.size());
            patentInventorService.create(patentInfo.getPid(), inventorNames);
            log.info("发明人信息保存完成");

            // 专利监控
            patentMonitorService.create(message);
            log.info("专利监控信息保存完成");

            log.info("========== 【专利消息处理结束】==========");

        } catch (Exception e) {
            log.error("========== 【专利消息处理异常】==========");
            log.error("消息内容: {}", message);
            log.error("异常信息: {}", e.getMessage(), e);
        }
    }

}
