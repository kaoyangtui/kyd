package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.UserFollowEntity;

import java.util.List;

public interface UserFollowService extends IService<UserFollowEntity> {
    List<Long> getFollowIds(String followType);
    Boolean follow(Long userId, String followType, Long followId);
    Boolean unfollow(Long userId, String followType, Long followId);
    Boolean isFollowed(Long userId, String followType, Long followId);
}