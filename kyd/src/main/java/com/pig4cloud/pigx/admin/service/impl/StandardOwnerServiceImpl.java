package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.StandardOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.StandardOwnerMapper;
import com.pig4cloud.pigx.admin.service.StandardOwnerService;
import com.pig4cloud.pigx.admin.vo.standard.StandardOwnerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标准起草单位
 *
 * @author pigx
 * @date 2025-05-15 19:19:25
 */
@Service
public class StandardOwnerServiceImpl extends ServiceImpl<StandardOwnerMapper, StandardOwnerEntity> implements StandardOwnerService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByStandardIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return this.lambdaUpdate().in(StandardOwnerEntity::getStandardId, ids).remove();
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean replaceOwners(Long standardId, List<StandardOwnerVO> owners) {
        // 删除原有
        this.lambdaUpdate().eq(StandardOwnerEntity::getStandardId, standardId).remove();
        if (CollUtil.isNotEmpty(owners)) {
            List<StandardOwnerEntity> entities = owners.stream()
                    .map(o -> {
                        StandardOwnerEntity entity = BeanUtil.copyProperties(o, StandardOwnerEntity.class);
                        entity.setStandardId(standardId);
                        return entity;
                    })
                    .collect(Collectors.toList());
            this.saveBatch(entities);
        }
        return Boolean.TRUE;
    }


}