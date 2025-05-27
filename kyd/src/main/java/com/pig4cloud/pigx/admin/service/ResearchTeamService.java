package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.*;
import com.pig4cloud.pigx.admin.entity.DemandInEntity;
import com.pig4cloud.pigx.admin.entity.ResearchTeamEntity;

import java.util.List;

/**
 * 科研团队服务接口
 * @author zhaoliang
 */
public interface ResearchTeamService extends IService<ResearchTeamEntity> {

    IPage<ResearchTeamResponse> pageResult(Page page, ResearchTeamPageRequest request);

    ResearchTeamResponse getDetail(Long id);

    Boolean create(ResearchTeamCreateRequest request);

    Boolean update(ResearchTeamUpdateRequest request);

    Boolean remove(List<Long> ids);

    Boolean shelfByIds(List<Long> ids, Integer shelfStatus);

    List<ResearchTeamResponse> exportList(ResearchTeamPageRequest request);

    ExportFieldListResponse exportFields();
}
