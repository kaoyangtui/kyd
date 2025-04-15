package com.pig4cloud.pigx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.entity.PatentLogEntity;
import com.pig4cloud.pigx.mapper.PatentLogMapper;
import com.pig4cloud.pigx.service.PatentLogService;
import org.springframework.stereotype.Service;
/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-15 17:10:58
 */
@Service
public class PatentLogServiceImpl extends ServiceImpl<PatentLogMapper, PatentLogEntity> implements PatentLogService {
}