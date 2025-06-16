package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.patent.PatentInfoResponse;
import com.pig4cloud.pigx.admin.entity.UserFollowEntity;
import com.pig4cloud.pigx.admin.mapper.UserFollowMapper;
import com.pig4cloud.pigx.admin.service.UserFollowService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关注表
 *
 * @author pigx
 * @date 2025-06-16 14:25:59
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollowEntity> implements UserFollowService {
    @Override
    public List<Long> getFollowIds(String followType) {
        return this.lambdaQuery()
                .select(UserFollowEntity::getFollowId)
                .eq(UserFollowEntity::getUserId, SecurityUtils.getUser().getId())
                .eq(UserFollowEntity::getFollowType, followType)
                .orderByDesc(UserFollowEntity::getCreateTime)
                .list()
                .stream()
                .map(UserFollowEntity::getFollowId)
                .collect(Collectors.toList());
    }
}