package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.ResultCompleterEntity;
import com.pig4cloud.pigx.admin.mapper.ResultCompleterMapper;
import com.pig4cloud.pigx.admin.service.ResultCompleterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成果披露—完成人
 *
 * @author pigx
 * @date 2025-05-22 10:44:25
 */
@Service
public class ResultCompleterServiceImpl extends ServiceImpl<ResultCompleterMapper, ResultCompleterEntity> implements ResultCompleterService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceCompleters(Long resultId, List<ResultCompleterEntity> entities) {
        this.remove(Wrappers.<ResultCompleterEntity>lambdaQuery()
                .eq(ResultCompleterEntity::getResultId, resultId));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setResultId(resultId));
            this.saveBatch(entities);
        }
    }
}