package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyRegOwnerMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyRegOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 软著登记著作权人信息
 *
 * @author pigx
 * @date 2025-05-13 10:53:55
 */
@Service
public class SoftCopyRegOwnerServiceImpl extends ServiceImpl<SoftCopyRegOwnerMapper, SoftCopyRegOwnerEntity> implements SoftCopyRegOwnerService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean replaceOwners(Long regId, List<SoftCopyRegOwnerEntity> owners) {
        // 先删
        this.lambdaUpdate().eq(SoftCopyRegOwnerEntity::getSoftCopyRegId, regId).remove();

        // 再插
        if (CollUtil.isNotEmpty(owners)) {
            owners.forEach(owner -> {
                owner.setSoftCopyRegId(regId);
                owner.setId(null);
            });
            this.saveBatch(owners);
        }

        return Boolean.TRUE;
    }

}