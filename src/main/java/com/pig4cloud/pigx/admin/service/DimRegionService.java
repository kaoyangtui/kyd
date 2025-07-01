package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DimRegionEntity;

import java.util.List;

public interface DimRegionService extends IService<DimRegionEntity> {
    List<Tree<Integer>> tree();
}