package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.dto.patentEvaluation.PatentEvaluationCommitRequest;
import com.pig4cloud.pigx.admin.entity.PatentEvaluationEntity;
import com.pig4cloud.pigx.admin.mapper.PatentEvaluationMapper;
import com.pig4cloud.pigx.admin.service.PatentEvaluationService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * 专利评估结果表
 *
 * @author pigx
 * @date 2025-08-07 09:35:50
 */
@RequiredArgsConstructor
@Service
public class PatentEvaluationServiceImpl extends ServiceImpl<PatentEvaluationMapper, PatentEvaluationEntity> implements PatentEvaluationService {

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean commit(PatentEvaluationCommitRequest request) {
        request.setSysFlag(TopicConstants.TOPIC_PATENT_TAG);
        // 全局顺序
        rocketMQTemplate.syncSendOrderly(TopicConstants.TOPIC_PATENT_EVALUATION, JSONUtil.toJsonStr(request), "global");
        return Boolean.TRUE;
    }
}