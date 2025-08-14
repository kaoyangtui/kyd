package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.toolkit.StrUtils;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.dto.researchProject.ProjectNameSearchRequest;
import com.pig4cloud.pigx.admin.dto.researchProject.ProjectTypeSearchRequest;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author zhaoliang
 */
@Tag(name = "维度信息管理")
@RestController
@RequestMapping("/dim")
@RequiredArgsConstructor
public class DimController {
    private final DimEcService dimEcService;
    private final DimAreaService dimAreaService;
    private final SysUserService sysUserService;
    private final DimMajorService dimMajorService;
    private final ResearchProjectService researchProjectService;
    private final FlowNodeService flowNodeService;

    @Operation(summary = "所属领域")
    @PostMapping("/ec/tree")
    public R<List<Tree<String>>> ecTree() {
        return R.ok(dimEcService.tree());
    }

    @Operation(summary = "所属地区")
    @PostMapping("/region/tree")
    public R<List<Tree<String>>> regionTree() {
        return R.ok(dimAreaService.tree());
    }

    @Operation(summary = "学科")
    @PostMapping("/major/tree")
    public R<List<Tree<String>>> majorTree() {
        return R.ok(dimMajorService.tree());
    }

    @Operation(summary = "获取用户列表")
    @GetMapping("/user")
    public R<List<SysUser>> getUserListByUserNameOrCode(String key) {
        // Check if the key is empty or null
        if (StrUtils.isBlank(key)) {
            return R.ok(Collections.emptyList());
        }

        // Query users by username or code (assuming code refers to "工号")
        List<SysUser> userList = sysUserService.list(Wrappers.<SysUser>lambdaQuery()
                .like(StrUtil.isNotBlank(key), SysUser::getUsername, key)
                .or()
                .like(StrUtil.isNotBlank(key), SysUser::getName, key)
        );
        return R.ok(userList);
    }

    @PostMapping("/project/type")
    @Operation(summary = "项目类型下拉模糊搜索")
    public R<List<String>> projectTypeOptions(@RequestBody ProjectTypeSearchRequest request) {
        return R.ok(researchProjectService.projectTypeOptions(request));
    }

    @PostMapping("/project/name")
    @Operation(summary = "项目名称下拉模糊搜索")
    public R<List<String>> projectNameOptions(@RequestBody ProjectNameSearchRequest request) {
        return R.ok(researchProjectService.projectNameOptions(request));
    }

    @GetMapping("/flow/node")
    @Operation(summary = "流程节点")
    public R<List<String>> flowNodeOptions(@RequestParam String flowKey) {
        //nodeName
        List<String> nodeNames = flowNodeService.lambdaQuery()
                .eq(FlowNode::getFlowKey, flowKey)
                .in(FlowNode::getNodeType, NodeTypeEnum.START.getType(), NodeTypeEnum.SERIAL.getType(), NodeTypeEnum.PARALLEL.getType(), NodeTypeEnum.END.getType())
                .orderByAsc(FlowNode::getNodeType, FlowNode::getSort, FlowNode::getId)
                .list()
                .stream()
                .map(FlowNode::getNodeName)
                .toList();
        return R.ok(nodeNames);
    }
}
