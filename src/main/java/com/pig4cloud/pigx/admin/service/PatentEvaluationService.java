package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patentEvaluation.PatentEvaluationCommitRequest;
import com.pig4cloud.pigx.admin.entity.PatentEvaluationEntity;

public interface PatentEvaluationService extends IService<PatentEvaluationEntity> {

    boolean commit(PatentEvaluationCommitRequest request);
}