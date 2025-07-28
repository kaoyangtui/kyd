package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.consult.*;
import com.pig4cloud.pigx.admin.entity.ConsultEntity;

import java.util.List;

public interface ConsultService extends IService<ConsultEntity> {

    Boolean create(ConsultCreateRequest request);

    Boolean update(ConsultUpdateRequest request);

    Boolean reply(ConsultReplyRequest request);

    ConsultResponse getDetail(Long id);

    Boolean removeByIds(List<Long> ids);

    IPage<ConsultResponse> pageResult(Page page, ConsultPageRequest request);

    ConsultResponse getDetailRead(Long id);
}