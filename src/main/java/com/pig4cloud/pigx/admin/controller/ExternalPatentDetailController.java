package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.entity.ExternalPatentDetailEntity;
import com.pig4cloud.pigx.admin.service.ExternalPatentDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 外部专利扩展信息表
 *
 * @author pigx
 * @date 2025-09-11 13:40:09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/externalPatentDetail" )
@Tag(description = "externalPatentDetail" , name = "外部专利扩展信息表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExternalPatentDetailController {

    private final  ExternalPatentDetailService externalPatentDetailService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param externalPatentDetail 外部专利扩展信息表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_view')" )
    public R getExternalPatentDetailPage(@ParameterObject Page page, @ParameterObject ExternalPatentDetailEntity externalPatentDetail) {
        LambdaQueryWrapper<ExternalPatentDetailEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(externalPatentDetailService.page(page, wrapper));
    }


    /**
     * 通过id查询外部专利扩展信息表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(externalPatentDetailService.getById(id));
    }

    /**
     * 新增外部专利扩展信息表
     * @param externalPatentDetail 外部专利扩展信息表
     * @return R
     */
    @Operation(summary = "新增外部专利扩展信息表" , description = "新增外部专利扩展信息表" )
    @SysLog("新增外部专利扩展信息表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_add')" )
    public R save(@RequestBody ExternalPatentDetailEntity externalPatentDetail) {
        return R.ok(externalPatentDetailService.save(externalPatentDetail));
    }

    /**
     * 修改外部专利扩展信息表
     * @param externalPatentDetail 外部专利扩展信息表
     * @return R
     */
    @Operation(summary = "修改外部专利扩展信息表" , description = "修改外部专利扩展信息表" )
    @SysLog("修改外部专利扩展信息表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_edit')" )
    public R updateById(@RequestBody ExternalPatentDetailEntity externalPatentDetail) {
        return R.ok(externalPatentDetailService.updateById(externalPatentDetail));
    }

    /**
     * 通过id删除外部专利扩展信息表
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除外部专利扩展信息表" , description = "通过id删除外部专利扩展信息表" )
    @SysLog("通过id删除外部专利扩展信息表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(externalPatentDetailService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param externalPatentDetail 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_externalPatentDetail_export')" )
    public List<ExternalPatentDetailEntity> export(ExternalPatentDetailEntity externalPatentDetail,Long[] ids) {
        return externalPatentDetailService.list(Wrappers.lambdaQuery(externalPatentDetail).in(ArrayUtil.isNotEmpty(ids), ExternalPatentDetailEntity::getId, ids));
    }
}