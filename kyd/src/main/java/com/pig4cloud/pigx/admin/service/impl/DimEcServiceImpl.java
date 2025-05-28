package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.DimEcEntity;
import com.pig4cloud.pigx.admin.mapper.DimEcMapper;
import com.pig4cloud.pigx.admin.service.DimEcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class DimEcServiceImpl extends ServiceImpl<DimEcMapper, DimEcEntity> implements DimEcService {

    @Override
    public List<Tree<String>> tree() {
        List<DimEcEntity> list = this.list();

        // 转为 TreeNode 列表
        List<TreeNode<String>> nodeList = list.stream().map(e -> {
            Map<String, Object> extra = new HashMap<>();
            extra.put("level", e.getLevel());
            extra.put("code", e.getCode());
            extra.put("pCode", e.getPCode());
            TreeNode<String> node = new TreeNode<>(
                    e.getCode(),
                    e.getPCode(),
                    e.getName(),
                    e.getLevel() != null ? e.getLevel() : 0
            );
            node.setExtra(extra); // 一次性设置 extra 字段
            return node;
        }).collect(Collectors.toList());

        // rootId: null 或 ""，看你的数据，建议用 null
        return TreeUtil.build(nodeList, "0");
    }
}
