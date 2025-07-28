package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.UserFollowEntity;

import java.util.List;

public interface UserFollowService extends IService<UserFollowEntity> {
    /**
     * 获取用户关注列表
     * @param followType 关注类型。专利：PATENT，成果：RESULT，企业需求：DEMAND，校内需求：DEMAND_IN，专家：EXPERT
     * @return
     */
    List<Long> getFollowIds(String followType);
    Boolean follow(Long userId, String followType, Long followId);
    Boolean unfollow(Long userId, String followType, Long followId);
    Boolean isFollowed(Long userId, String followType, Long followId);
}