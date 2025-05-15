package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.StandardEntity;
import com.pig4cloud.pigx.admin.vo.standard.*;

import java.util.List;

public interface StandardService extends IService<StandardEntity> {
    IPage<StandardResponse> pageResult(StandardPageRequest request);

    StandardResponse getDetail(Long id);

    Boolean createStandard(StandardCreateRequest request);

    Boolean updateStandard(StandardUpdateRequest request);

    Boolean removeStandards(List<Long> ids);

    List<StandardResponse> exportList(StandardPageRequest request);

}