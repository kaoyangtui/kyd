package com.pig4cloud.pigx.admin.service;

import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamMemberVO;

import java.util.List;

public interface ResearchTeamMemberService {

    /**
     * 替换指定团队的成员信息
     */
    void replaceMembers(Long teamId, List<ResearchTeamMemberVO> memberList);

    /**
     * 删除指定团队的所有成员
     */
    void removeByTeamIds(List<Long> teamIds);
}
