package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.perf.PerfSchemeCellResponse;
import com.pig4cloud.pigx.admin.dto.perf.PerfSchemeOverviewResponse;
import com.pig4cloud.pigx.admin.entity.PerfRuleResultEntity;
import com.pig4cloud.pigx.admin.entity.PerfSchemeEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PerfRuleResultMapper;
import com.pig4cloud.pigx.admin.service.PerfRuleResultService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 用户业绩方案得分
 *
 * @author pigx
 * @date 2025-09-07 17:36:52
 */
@Service
public class PerfRuleResultServiceImpl extends ServiceImpl<PerfRuleResultMapper, PerfRuleResultEntity> implements PerfRuleResultService {

}