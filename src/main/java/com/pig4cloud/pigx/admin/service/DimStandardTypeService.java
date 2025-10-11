package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.DimStandardTypeEntity;

import java.util.List;

public interface DimStandardTypeService extends IService<DimStandardTypeEntity> {

    List<Tree<String>> tree();
}