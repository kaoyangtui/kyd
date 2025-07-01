package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.mapper.CompleterMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 完成人
 *
 * @author pigx
 * @date 2025-05-23 14:31:27
 */
@Service
public class CompleterServiceImpl extends ServiceImpl<CompleterMapper, CompleterEntity> implements CompleterService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceCompleters(String code, List<CompleterEntity> entities) {
        this.remove(Wrappers.<CompleterEntity>lambdaQuery()
                .eq(CompleterEntity::getCode, code));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setCode(code));
            this.saveBatch(entities);
        }
    }
}