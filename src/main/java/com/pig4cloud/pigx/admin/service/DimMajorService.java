package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DimMajorEntity;

import java.util.List;

public interface DimMajorService extends IService<DimMajorEntity> {
    List<Tree<String>> tree();
}