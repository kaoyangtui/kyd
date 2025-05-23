package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.StandardDrafterInEntity;
import com.pig4cloud.pigx.admin.mapper.StandardDrafterInMapper;
import com.pig4cloud.pigx.admin.service.StandardDrafterInService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标准校内起草人信息
 *
 * @author pigx
 * @date 2025-05-15 19:19:02
 */
@Service
public class StandardDrafterInServiceImpl extends ServiceImpl<StandardDrafterInMapper, StandardDrafterInEntity> implements StandardDrafterInService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByStandardIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return this.lambdaUpdate().in(StandardDrafterInEntity::getStandardId, ids).remove();
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean replaceDrafters(Long standardId, List<StandardDrafterInVO> drafters) {
        // 删除原有
        this.lambdaUpdate().eq(StandardDrafterInEntity::getStandardId, standardId).remove();
        if (CollUtil.isNotEmpty(drafters)) {
            List<StandardDrafterInEntity> entities = drafters.stream()
                    .map(d -> {
                        StandardDrafterInEntity entity = BeanUtil.copyProperties(d, StandardDrafterInEntity.class);
                        entity.setStandardId(standardId);
                        return entity;
                    })
                    .collect(Collectors.toList());
            this.saveBatch(entities);
        }
        return Boolean.TRUE;
    }

}