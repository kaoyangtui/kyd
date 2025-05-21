package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.StandardDrafterInEntity;
import com.pig4cloud.pigx.admin.dto.standard.StandardDrafterInVO;

import java.util.List;

public interface StandardDrafterInService extends IService<StandardDrafterInEntity> {

    Boolean removeByStandardIds(List<Long> ids);

    Boolean replaceDrafters(Long id, List<StandardDrafterInVO> drafters);
}