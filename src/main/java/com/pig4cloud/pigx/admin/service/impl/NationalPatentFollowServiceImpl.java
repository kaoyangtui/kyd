package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.nationalPatent.NationalPatentFollowCreateRequest;
import com.pig4cloud.pigx.admin.entity.NationalPatentFollowEntity;
import com.pig4cloud.pigx.admin.entity.NationalPatentInfoEntity;
import com.pig4cloud.pigx.admin.mapper.NationalPatentFollowMapper;
import com.pig4cloud.pigx.admin.mapper.NationalPatentInfoMapper;
import com.pig4cloud.pigx.admin.service.NationalPatentFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NationalPatentFollowServiceImpl
        extends ServiceImpl<NationalPatentFollowMapper, NationalPatentFollowEntity>
        implements NationalPatentFollowService {

    private final NationalPatentInfoMapper nationalPatentInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean follow(Long userId, NationalPatentFollowCreateRequest req) {
        // 是否已存在
        NationalPatentFollowEntity exist = this.lambdaQuery()
                .eq(NationalPatentFollowEntity::getUserId, userId)
                .eq(NationalPatentFollowEntity::getPid, req.getPid())
                .one();

        // 拉主表，构造 tags
        NationalPatentInfoEntity info = nationalPatentInfoMapper.selectOne(
                Wrappers.<NationalPatentInfoEntity>lambdaQuery()
                        .eq(NationalPatentInfoEntity::getPid, req.getPid())
                        .last("limit 1")
        );

        String note = toNullIfBlank(req.getNote());
        String tags = buildTags(info, req.getTags());

        if (exist != null) {
            NationalPatentFollowEntity patch = new NationalPatentFollowEntity();
            patch.setId(exist.getId());
            patch.setNote(note);
            patch.setTags(tags); // 用最新合并结果覆盖
            return this.updateById(patch);
        }

        NationalPatentFollowEntity e = new NationalPatentFollowEntity();
        e.setUserId(userId);
        e.setPid(req.getPid());
        e.setNote(note);
        e.setTags(tags);
        e.setDelFlag("0");
        return this.save(e);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfollow(Long userId, String pid) {
        return this.lambdaUpdate()
                .eq(NationalPatentFollowEntity::getUserId, userId)
                .eq(NationalPatentFollowEntity::getPid, pid)
                .remove();
    }

    @Override
    public Set<String> listFollowedPids(Long userId, List<String> pids) {
        if (CollUtil.isEmpty(pids)) return Collections.emptySet();
        return this.lambdaQuery()
                .eq(NationalPatentFollowEntity::getUserId, userId)
                .in(NationalPatentFollowEntity::getPid, pids)
                .list()
                .stream()
                .map(NationalPatentFollowEntity::getPid)
                .collect(Collectors.toSet());
    }

    private String toNullIfBlank(String s) {
        return StrUtil.isBlank(s) ? null : s.trim();
    }

    /** 组合 tags：用户自定义 + 标题 + 申请号 + 公开号 + 申请人（分号分隔，去重/去空/去多余分号） */
    private String buildTags(NationalPatentInfoEntity info, String userTags) {
        List<String> parts = new ArrayList<>(8);
        if (StrUtil.isNotBlank(userTags)) {
            // 允许用户传入“自定义标签”，支持用 ; 分隔多项
            for (String t : StrUtil.split(userTags, ';')) {
                if (StrUtil.isNotBlank(t)) parts.add(t.trim());
            }
        }
        if (info != null) {
            add(parts, info.getTitle());
            add(parts, info.getAppNumber());
            add(parts, info.getPubNumber());
            add(parts, info.getApplicantName());
        }
        // 去重、过滤空白
        LinkedHashSet<String> set = parts.stream()
                .filter(StrUtil::isNotBlank)
                .map(String::trim)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return String.join(";", set);
    }
    private void add(List<String> parts, String val) {
        if (StrUtil.isNotBlank(val)) parts.add(val.trim());
    }
}
