package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentDetailEntity;
import com.pig4cloud.pigx.admin.mapper.PatentDetailMapper;
import com.pig4cloud.pigx.admin.service.PatentDetailService;
import org.springframework.stereotype.Service;

/**
 * 专利扩展信息表
 *
 * @author pigx
 * @date 2025-05-31 10:46:09
 */
@Service
public class PatentDetailServiceImpl extends ServiceImpl<PatentDetailMapper, PatentDetailEntity> implements PatentDetailService {
}