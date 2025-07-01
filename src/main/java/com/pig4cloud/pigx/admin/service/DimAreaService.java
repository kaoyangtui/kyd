package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DimAreaEntity;

import java.util.List;

public interface DimAreaService extends IService<DimAreaEntity> {
    List<Tree<String>> tree();
}