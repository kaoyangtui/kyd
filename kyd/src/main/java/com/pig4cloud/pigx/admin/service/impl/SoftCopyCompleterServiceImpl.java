package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyCompleterMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyCompleterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 软著提案—完成人
 *
 * @author pigx
 * @date 2025-05-13 08:17:51
 */
@Service
public class SoftCopyCompleterServiceImpl extends ServiceImpl<SoftCopyCompleterMapper, SoftCopyCompleterEntity> implements SoftCopyCompleterService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceCompleters(Long softCopyId, List<SoftCopyCompleterEntity> entities) {
        this.remove(Wrappers.<SoftCopyCompleterEntity>lambdaQuery().eq(SoftCopyCompleterEntity::getSoftCopyId, softCopyId));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setSoftCopyId(softCopyId));
            this.saveBatch(entities);
        }
    }

}