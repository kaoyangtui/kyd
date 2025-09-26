package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowCreateRequest;
import com.pig4cloud.pigx.admin.entity.NationalPatentFollowEntity;

import java.util.List;
import java.util.Set;

public interface NationalPatentFollowService extends IService<NationalPatentFollowEntity> {

    /**
     * 关注全国专利（幂等：已关注则更新备注/标签）
     * @param userId 当前用户ID（SecurityUtils.getUser().getId()）
     * @param req    关注请求（包含 pid、note、tags）
     * @return true 成功
     */
    boolean follow(Long userId, NationalPatentFollowCreateRequest req);

    /**
     * 取消关注全国专利
     * @param userId 当前用户ID
     * @param pid    专利PID
     * @return true 成功
     */
    boolean unfollow(Long userId, String pid);

    Set<String> listFollowedPids(Long userId, List<String> pids);
}