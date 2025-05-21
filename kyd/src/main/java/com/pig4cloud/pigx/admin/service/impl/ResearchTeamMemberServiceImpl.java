package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamMemberVO;
import com.pig4cloud.pigx.admin.entity.ResearchTeamMemberEntity;
import com.pig4cloud.pigx.admin.mapper.ResearchTeamMemberMapper;
import com.pig4cloud.pigx.admin.service.ResearchTeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class ResearchTeamMemberServiceImpl extends ServiceImpl<ResearchTeamMemberMapper, ResearchTeamMemberEntity>
        implements ResearchTeamMemberService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceMembers(Long teamId, List<ResearchTeamMemberVO> memberList) {
        this.remove(Wrappers.<ResearchTeamMemberEntity>lambdaQuery()
                .eq(ResearchTeamMemberEntity::getTeamId, teamId));

        if (CollUtil.isNotEmpty(memberList)) {
            List<ResearchTeamMemberEntity> entityList = BeanUtil.copyToList(memberList, ResearchTeamMemberEntity.class);
            entityList.forEach(item -> item.setTeamId(teamId));
            this.saveBatch(entityList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByTeamIds(List<Long> teamIds) {
        if (CollUtil.isEmpty(teamIds)) {
            return;
        }

        this.remove(Wrappers.<ResearchTeamMemberEntity>lambdaQuery()
                .in(ResearchTeamMemberEntity::getTeamId, teamIds));
    }
}
