package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.perf.PerfRuleCalcRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfRuleCalcSummary;
import com.pig4cloud.pigx.admin.entity.PerfRuleResultEntity;

public interface PerfRuleResultService extends IService<PerfRuleResultEntity> {

    PerfRuleCalcSummary runCalc(PerfRuleCalcRequest req);

}