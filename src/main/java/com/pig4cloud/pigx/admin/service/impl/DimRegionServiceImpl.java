package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.DimRegionEntity;
import com.pig4cloud.pigx.admin.mapper.DimRegionMapper;
import com.pig4cloud.pigx.admin.service.DimRegionService;
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
public class DimRegionServiceImpl extends ServiceImpl<DimRegionMapper, DimRegionEntity> implements DimRegionService {

    @Override
    public List<Tree<Integer>> tree() {
        List<DimRegionEntity> list = this.list();

        List<TreeNode<Integer>> nodeList = list.stream().map(e -> {
            Map<String, Object> extra = new HashMap<>();
            extra.put("ctype", e.getCtype());
            extra.put("name", e.getCname());

            TreeNode<Integer> node = new TreeNode<>(
                    e.getId(),
                    e.getParentId(),
                    e.getCname(),
                    e.getCtype() == null ? 0 : e.getCtype()
            );
            node.setExtra(extra);
            return node;
        }).collect(Collectors.toList());

        // 你的数据 parent_id=0 是根节点
        return TreeUtil.build(nodeList, 1);
    }
}
