package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.DimStandardTypeEntity;
import com.pig4cloud.pigx.admin.mapper.DimStandardTypeMapper;
import com.pig4cloud.pigx.admin.service.DimStandardTypeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标准类型
 *
 * @author pigx
 * @date 2025-10-11 08:59:58
 */
@Service
public class DimStandardTypeServiceImpl extends ServiceImpl<DimStandardTypeMapper, DimStandardTypeEntity> implements DimStandardTypeService {
    @Override
    public List<Tree<String>> tree() {
        List<DimStandardTypeEntity> list = this.list();

        List<TreeNode<String>> nodeList = list.stream().map(e -> {
            Map<String, Object> extra = new HashMap<>();
            extra.put("code", e.getCode());
            extra.put("level", e.getLevel());
            TreeNode<String> node = new TreeNode<>(
                    e.getId().toString(),
                    e.getParentId().toString(),
                    e.getName(),
                    e.getId()
            );
            node.setExtra(extra);
            return node;
        }).collect(Collectors.toList());

        // 如果 parent_id 为 null 顶级，rootId 设为 null，否则根据实际数据
        return TreeUtil.build(nodeList, "0");
    }
}