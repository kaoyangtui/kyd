package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.mapper.OwnerMapper;
import com.pig4cloud.pigx.admin.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 申请人
 *
 * @author pigx
 * @date 2025-05-23 14:32:10
 */
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, OwnerEntity> implements OwnerService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceOwners(String code, List<OwnerEntity> entities) {
        this.remove(Wrappers.<OwnerEntity>lambdaQuery()
                .eq(OwnerEntity::getCode, code));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> {
                e.setId(null);
                e.setCode(code);
            });
            this.saveBatch(entities);
        }
    }
}