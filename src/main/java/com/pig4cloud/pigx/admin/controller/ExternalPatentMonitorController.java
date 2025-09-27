package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.entity.ExternalPatentMonitorEntity;
import com.pig4cloud.pigx.admin.service.ExternalPatentMonitorService;
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
 * 专利监控表
 *
 * @author pigx
 * @date 2025-09-11 13:37:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/externalPatentMonitor" )
@Tag(description = "externalPatentMonitor" , name = "专利监控表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExternalPatentMonitorController {

    private final  ExternalPatentMonitorService externalPatentMonitorService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param externalPatentMonitor 专利监控表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    public R getExternalPatentMonitorPage(@ParameterObject Page page, @ParameterObject ExternalPatentMonitorEntity externalPatentMonitor) {
        LambdaQueryWrapper<ExternalPatentMonitorEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(externalPatentMonitorService.page(page, wrapper));
    }


    /**
     * 通过id查询专利监控表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(externalPatentMonitorService.getById(id));
    }

    /**
     * 新增专利监控表
     * @param externalPatentMonitor 专利监控表
     * @return R
     */
    @Operation(summary = "新增专利监控表" , description = "新增专利监控表" )
    @SysLog("新增专利监控表" )
    @PostMapping
    public R save(@RequestBody ExternalPatentMonitorEntity externalPatentMonitor) {
        return R.ok(externalPatentMonitorService.save(externalPatentMonitor));
    }

    /**
     * 修改专利监控表
     * @param externalPatentMonitor 专利监控表
     * @return R
     */
    @Operation(summary = "修改专利监控表" , description = "修改专利监控表" )
    @SysLog("修改专利监控表" )
    @PutMapping
    public R updateById(@RequestBody ExternalPatentMonitorEntity externalPatentMonitor) {
        return R.ok(externalPatentMonitorService.updateById(externalPatentMonitor));
    }

    /**
     * 通过id删除专利监控表
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除专利监控表" , description = "通过id删除专利监控表" )
    @SysLog("通过id删除专利监控表" )
    @DeleteMapping
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(externalPatentMonitorService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param externalPatentMonitor 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    public List<ExternalPatentMonitorEntity> export(ExternalPatentMonitorEntity externalPatentMonitor,Long[] ids) {
        return externalPatentMonitorService.list(Wrappers.lambdaQuery(externalPatentMonitor).in(ArrayUtil.isNotEmpty(ids), ExternalPatentMonitorEntity::getId, ids));
    }
}