package com.pig4cloud.pigx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.entity.Draft;
import com.pig4cloud.pigx.mapper.DraftMapper;
import com.pig4cloud.pigx.service.DraftService;
import org.springframework.stereotype.Service;

/**
 * 草稿管理表，用于管理用户草稿数据
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
@Service
public class DraftServiceImpl extends ServiceImpl<DraftMapper, Draft> implements DraftService {
}