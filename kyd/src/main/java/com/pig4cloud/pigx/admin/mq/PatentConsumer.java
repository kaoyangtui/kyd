package com.pig4cloud.pigx.admin.mq;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.utils.RocketMQUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        endpoints = "${rocketmq.producer.endpoints}",
        namespace = "${rocketmq.producer.namespace}",
        accessKey = "${rocketmq.producer.accessKey}",
        secretKey = "${rocketmq.producer.secretKey}",
        topic = TopicConstants.TOPIC_PATENT,
        consumerGroup = TopicConstants.CONSUMER_GROUP_PATENT,
        tag = "*"
)
public class PatentConsumer implements RocketMQListener {

    private final PatentInfoService patentInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsumeResult consume(MessageView messageView) {
        try {
            String body = RocketMQUtils.getMessageBody(messageView.getBody());
            // 检查消息字符串是否为空
            if (StrUtil.isBlank(body)) {
                log.warn("消息为空");
                return null;
            }
            PatentInfoEntity patentInfo = JSONUtil.toBean(body, PatentInfoEntity.class);
            patentInfo.setId(null);
            PatentInfoEntity oldPatentInfo = patentInfoService.lambdaQuery()
                    .eq(PatentInfoEntity::getPid, patentInfo.getPid())
                    .one();
            if (oldPatentInfo != null) {
                BeanUtil.copyProperties(patentInfo, oldPatentInfo, true);
                patentInfoService.updateById(oldPatentInfo);
            } else {
                patentInfoService.save(patentInfo);
            }
        } catch (Exception e) {
            log.info("消费消息: {}", JSONUtil.toJsonStr(messageView));
            log.error("消费消息异常: {}", e.getMessage(), e);
        }
        return ConsumeResult.SUCCESS;
    }
}
