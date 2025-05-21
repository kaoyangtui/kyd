package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.StandardEntity;
import com.pig4cloud.pigx.admin.dto.standard.*;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface StandardService extends IService<StandardEntity> {
    IPage<StandardResponse> pageResult(Page page, StandardPageRequest request);

    StandardResponse getDetail(Long id);

    Boolean createStandard(StandardCreateRequest request);

    Boolean updateStandard(StandardUpdateRequest request);

    Boolean removeStandards(List<Long> ids);

}