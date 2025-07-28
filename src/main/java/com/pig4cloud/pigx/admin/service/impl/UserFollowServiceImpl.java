package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.UserFollowEntity;
import com.pig4cloud.pigx.admin.mapper.UserFollowMapper;
import com.pig4cloud.pigx.admin.service.UserFollowService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public Boolean follow(Long userId, String followType, Long followId) {
        LambdaQueryWrapper<UserFollowEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(UserFollowEntity::getUserId, userId)
                .eq(UserFollowEntity::getFollowType, followType)
                .eq(UserFollowEntity::getFollowId, followId);
        UserFollowEntity exist = this.getOne(qw, false);
        if (exist != null) {
            // 已关注，无需重复关注
            return true;
        }
        UserFollowEntity entity = new UserFollowEntity();
        entity.setUserId(userId);
        entity.setFollowType(followType);
        entity.setFollowId(followId);
        return this.save(entity);
    }

    @Override
    public Boolean unfollow(Long userId, String followType, Long followId) {
        return this.remove(
                new LambdaQueryWrapper<UserFollowEntity>()
                        .eq(UserFollowEntity::getUserId, userId)
                        .eq(UserFollowEntity::getFollowType, followType)
                        .eq(UserFollowEntity::getFollowId, followId)
        );
    }

    @Override
    public Boolean isFollowed(Long userId, String followType, Long followId) {
        return this.count(
                new LambdaQueryWrapper<UserFollowEntity>()
                        .eq(UserFollowEntity::getUserId, userId)
                        .eq(UserFollowEntity::getFollowType, followType)
                        .eq(UserFollowEntity::getFollowId, followId)
        ) > 0;
    }
}