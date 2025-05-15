package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.IcLayoutOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.IcLayoutOwnerMapper;
import com.pig4cloud.pigx.admin.service.IcLayoutOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 集成电路布图权利人信息
 *
 * @author pigx
 * @date 2025-05-15 12:13:49
 */
@Service
public class IcLayoutOwnerServiceImpl extends ServiceImpl<IcLayoutOwnerMapper, IcLayoutOwnerEntity> implements IcLayoutOwnerService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByIcLayoutIds(List<Long> icLayoutIds) {
        this.remove(new LambdaQueryWrapper<IcLayoutOwnerEntity>()
                .in(IcLayoutOwnerEntity::getIcLayoutId, icLayoutIds));
        return Boolean.TRUE;
    }

}