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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = TopicConstants.TOPIC_PATENT,
        consumerGroup = TopicConstants.CONSUMER_GROUP_PATENT + "_" + "${spring.profiles.active}",
        selectorExpression = "${spring.profiles.active}"
)
public class PatentConsumer implements RocketMQListener<String> {

    private final PatentInfoService patentInfoService;
    private final PatentDetailService patentDetailService;
    private final PatentInventorService patentInventorService;
    private final PatentMonitorService patentMonitorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String message) {
        try {
            if (StrUtil.isBlank(message)) {
                log.warn("消息为空");
                return;
            }
            PatentInfoEntity patentInfo = JSONUtil.toBean(message, PatentInfoEntity.class);
            //主表信息保存
            patentInfoService.create(patentInfo, message);
            //详情信息保存
            PatentDetailEntity patentDetail = JSONUtil.toBean(message, PatentDetailEntity.class);
            patentDetail.setId(null);
            patentDetail.setCitationForwardCountry(CodeUtils.formatCodes(patentDetail.getCitationForwardCountry()));
            patentDetailService.save(patentDetail);
            //发明人
            patentInventorService.create(patentInfo.getPid(), StrUtil.split(patentInfo.getInventorName(), ';'));
            //专利监控
            patentMonitorService.create(message);
        } catch (Exception e) {
            log.info("消费消息: {}", message);
            log.error("消费消息异常: {}", e.getMessage(), e);
        }
    }
}
