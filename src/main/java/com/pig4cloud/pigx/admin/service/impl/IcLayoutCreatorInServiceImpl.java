package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.IcLayoutCreatorInEntity;
import com.pig4cloud.pigx.admin.mapper.IcLayoutCreatorInMapper;
import com.pig4cloud.pigx.admin.service.IcLayoutCreatorInService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 集成电路布图校内创作人信息
 *
 * @author pigx
 * @date 2025-05-15 12:13:21
 */
@Service
public class IcLayoutCreatorInServiceImpl extends ServiceImpl<IcLayoutCreatorInMapper, IcLayoutCreatorInEntity> implements IcLayoutCreatorInService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByIcLayoutIds(List<Long> icLayoutIds) {
        this.remove(new LambdaQueryWrapper<IcLayoutCreatorInEntity>()
                .in(IcLayoutCreatorInEntity::getIcLayoutId, icLayoutIds));
        return Boolean.TRUE;
    }

}