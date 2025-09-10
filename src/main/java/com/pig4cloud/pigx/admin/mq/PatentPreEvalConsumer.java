package com.pig4cloud.pigx.admin.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.dto.PatentPreEval.PatentEvaluationMqResponse;
import com.pig4cloud.pigx.admin.dto.PatentPreEval.PatentPreEvalUpdateResultRequest;
import com.pig4cloud.pigx.admin.entity.PatentPreEvalEntity;
import com.pig4cloud.pigx.admin.service.PatentPreEvalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 专利申请前评估结果消费
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = TopicConstants.TOPIC_PATENT_EVALUATION_RESULT,
        consumerGroup = TopicConstants.CONSUMER_GROUP_PATENT_EVALUATION_RESULT + "_" + TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}",
        selectorExpression = TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}",
        consumeMode = ConsumeMode.ORDERLY
)
public class PatentPreEvalConsumer implements RocketMQListener<String> {

    private final PatentPreEvalService patentPreEvalService;

    @Override
    public void onMessage(String message) {
        if (StrUtil.isBlank(message)) {
            log.warn("[PPE-RESULT] 空消息，跳过处理");
            return;
        }
        try {
            // 1) 直接反序列化
            PatentEvaluationMqResponse mq = JSONUtil.toBean(message, PatentEvaluationMqResponse.class);
            log.debug("[PPE-RESULT] 收到: {}", mq);

            if (mq == null || StrUtil.isBlank(mq.getSysBizId())) {
                log.error("[PPE-RESULT] 消息缺少 sysBizId，无法定位目标：{}", message);
                return;
            }

            // 2) 用 sysBizId 定位记录：优先当作 Long id，其次按 code 查询
            Long id = tryParseLong(mq.getSysBizId());
            if (id == null) {
                PatentPreEvalEntity hit = patentPreEvalService.lambdaQuery()
                        .select(PatentPreEvalEntity::getId)
                        .eq(PatentPreEvalEntity::getCode, mq.getSysBizId())
                        .one();
                if (hit != null) {
                    id = hit.getId();
                }
            }
            if (id == null) {
                log.error("[PPE-RESULT] 未找到匹配记录：sysBizId={}", mq.getSysBizId());
                return;
            }

            // 3) 组装仅更新“结果字段”的请求并调用
            PatentPreEvalUpdateResultRequest req = new PatentPreEvalUpdateResultRequest();
            req.setId(id);
            req.setStatus(0);
            req.setLevel(mq.getLevel());
            req.setReportDate(mq.getReportDate());
            req.setViability(mq.getViability());
            req.setTech(mq.getTech());
            req.setMarket(mq.getMarket());
            req.setReportUrl(mq.getReportUrl());

            boolean ok = patentPreEvalService.updateResult(req);
            log.info("[PPE-RESULT] 更新评估结果 {}，id={} level={} reportDate={}",
                    ok ? "成功" : "失败", id, req.getLevel(), req.getReportDate());

        } catch (Exception e) {
            log.error("[PPE-RESULT] 处理异常，msg={}, ex={}", message, e.getMessage(), e);
        }
    }

    private static Long tryParseLong(String s) {
        try {
            return StrUtil.isBlank(s) ? null : Long.parseLong(s.trim());
        } catch (Exception ignore) {
            return null;
        }
    }
}
