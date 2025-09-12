package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.ExternalPatentMonitorSubjectEntity;
import com.pig4cloud.pigx.admin.mapper.ExternalPatentMonitorSubjectMapper;
import com.pig4cloud.pigx.admin.service.ExternalPatentMonitorSubjectService;
import org.springframework.stereotype.Service;
/**
 * 监控主表（企业/技术汇总去重 + 统计）
 *
 * @author pigx
 * @date 2025-09-11 13:37:13
 */
@Service
public class ExternalPatentMonitorSubjectServiceImpl extends ServiceImpl<ExternalPatentMonitorSubjectMapper, ExternalPatentMonitorSubjectEntity> implements ExternalPatentMonitorSubjectService {
}