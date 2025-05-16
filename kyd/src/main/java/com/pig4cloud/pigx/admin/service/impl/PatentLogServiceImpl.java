package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.mapper.PatentLogMapper;
import com.pig4cloud.pigx.admin.service.PatentLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-15 17:10:58
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PatentLogServiceImpl extends ServiceImpl<PatentLogMapper, PatentLogEntity> implements PatentLogService {

}