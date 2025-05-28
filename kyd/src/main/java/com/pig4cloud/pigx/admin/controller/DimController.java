package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import com.pig4cloud.pigx.admin.service.DimEcService;
import com.pig4cloud.pigx.admin.service.DimRegionService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    private final DimRegionService dimRegionService;

    @Operation(summary = "所属领域")
    @PostMapping("/ec/tree")
    public R<List<Tree<String>>> ecTree() {
        return R.ok(dimEcService.tree());
    }

    @Operation(summary = "所属地区")
    @PostMapping("/region/tree")
    public R<List<Tree<Integer>>> regionTree() {
        return R.ok(dimRegionService.tree());
    }
}
