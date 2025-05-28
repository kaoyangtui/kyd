package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DimEcEntity;

import java.util.List;

public interface DimEcService extends IService<DimEcEntity> {
    List<Tree<String>> tree();

}