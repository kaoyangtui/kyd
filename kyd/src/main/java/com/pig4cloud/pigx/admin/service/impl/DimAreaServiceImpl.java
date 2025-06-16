package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.DimAreaEntity;
import com.pig4cloud.pigx.admin.mapper.DimAreaMapper;
import com.pig4cloud.pigx.admin.service.DimAreaService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 行政区
 *
 * @author pigx
 * @date 2025-06-16 18:32:36
 */
@Service
public class DimAreaServiceImpl extends ServiceImpl<DimAreaMapper, DimAreaEntity> implements DimAreaService {
    @Override
    public List<Tree<String>> tree() {
        List<DimAreaEntity> list = this.lambdaQuery().in(DimAreaEntity::getLevel, 1, 2, 3).list();

        List<TreeNode<String>> nodeList = list.stream().map(e -> {
            Map<String, Object> extra = new HashMap<>();
            extra.put("name", e.getName());
            extra.put("level", e.getLevel());

            TreeNode<String> node = new TreeNode<>(
                    e.getCode().toString(),
                    e.getPcode().toString(),
                    e.getName(),
                    e.getCode()
            );
            node.setExtra(extra);
            return node;
        }).collect(Collectors.toList());

        // 你的数据 parent_id=0 是根节点
        return TreeUtil.build(nodeList, "0");
    }
}