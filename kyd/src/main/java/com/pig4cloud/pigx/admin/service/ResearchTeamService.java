package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.researchTeam.*;

import java.util.List;

public interface ResearchTeamService {

    /**
     * 创建科研团队（含成员）
     */
    ResearchTeamResponse createTeam(ResearchTeamCreateRequest request);

    /**
     * 更新科研团队（含成员）
     */
    Boolean updateTeam(ResearchTeamUpdateRequest request);

    /**
     * 查询科研团队详情（含成员）
     */
    ResearchTeamResponse getDetail(Long id);

    /**
     * 分页查询科研团队
     */
    IPage<ResearchTeamResponse> pageResult(Page page, ResearchTeamPageRequest request);

    /**
     * 删除科研团队及其成员
     */
    Boolean removeTeam(IdListRequest request);

    /**
     * 更新上下架状态
     */
    Boolean updateShelfStatus(ResearchTeamShelfRequest request);
}
