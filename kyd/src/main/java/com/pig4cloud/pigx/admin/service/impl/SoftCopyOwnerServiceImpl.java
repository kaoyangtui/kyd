package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyOwnerMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 软著提案—著作权人
 *
 * @author pigx
 * @date 2025-05-13 08:16:31
 */
@Service
public class SoftCopyOwnerServiceImpl extends ServiceImpl<SoftCopyOwnerMapper, SoftCopyOwnerEntity> implements SoftCopyOwnerService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceOwners(Long softCopyId, List<SoftCopyOwnerEntity> entities) {
        this.remove(Wrappers.<SoftCopyOwnerEntity>lambdaQuery().eq(SoftCopyOwnerEntity::getSoftCopyId, softCopyId));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setSoftCopyId(softCopyId));
            this.saveBatch(entities);
        }
    }
}