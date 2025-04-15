package com.pig4cloud.pigx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.entity.PatentInfoEntity;
import com.pig4cloud.pigx.mapper.PatentInfoMapper;
import com.pig4cloud.pigx.service.PatentInfoService;
import org.springframework.stereotype.Service;
/**
 * 专利信息表
 *
 * @author zl
 * @date 2025-04-15 13:09:58
 */
@Service
public class PatentInfoServiceImpl extends ServiceImpl<PatentInfoMapper, PatentInfoEntity> implements PatentInfoService {
}