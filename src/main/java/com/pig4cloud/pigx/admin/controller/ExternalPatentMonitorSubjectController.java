package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.entity.ExternalPatentMonitorSubjectEntity;
import com.pig4cloud.pigx.admin.service.ExternalPatentMonitorSubjectService;
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
 * 监控主表（企业/技术汇总去重 + 统计）
 *
 * @author pigx
 * @date 2025-09-11 13:37:13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/externalPatentMonitorSubject" )
@Tag(description = "externalPatentMonitorSubject" , name = "监控主表（企业/技术汇总去重 + 统计）管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExternalPatentMonitorSubjectController {

    private final  ExternalPatentMonitorSubjectService externalPatentMonitorSubjectService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param externalPatentMonitorSubject 监控主表（企业/技术汇总去重 + 统计）
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_view')" )
    public R getExternalPatentMonitorSubjectPage(@ParameterObject Page page, @ParameterObject ExternalPatentMonitorSubjectEntity externalPatentMonitorSubject) {
        LambdaQueryWrapper<ExternalPatentMonitorSubjectEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(externalPatentMonitorSubjectService.page(page, wrapper));
    }


    /**
     * 通过id查询监控主表（企业/技术汇总去重 + 统计）
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(externalPatentMonitorSubjectService.getById(id));
    }

    /**
     * 新增监控主表（企业/技术汇总去重 + 统计）
     * @param externalPatentMonitorSubject 监控主表（企业/技术汇总去重 + 统计）
     * @return R
     */
    @Operation(summary = "新增监控主表（企业/技术汇总去重 + 统计）" , description = "新增监控主表（企业/技术汇总去重 + 统计）" )
    @SysLog("新增监控主表（企业/技术汇总去重 + 统计）" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_add')" )
    public R save(@RequestBody ExternalPatentMonitorSubjectEntity externalPatentMonitorSubject) {
        return R.ok(externalPatentMonitorSubjectService.save(externalPatentMonitorSubject));
    }

    /**
     * 修改监控主表（企业/技术汇总去重 + 统计）
     * @param externalPatentMonitorSubject 监控主表（企业/技术汇总去重 + 统计）
     * @return R
     */
    @Operation(summary = "修改监控主表（企业/技术汇总去重 + 统计）" , description = "修改监控主表（企业/技术汇总去重 + 统计）" )
    @SysLog("修改监控主表（企业/技术汇总去重 + 统计）" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_edit')" )
    public R updateById(@RequestBody ExternalPatentMonitorSubjectEntity externalPatentMonitorSubject) {
        return R.ok(externalPatentMonitorSubjectService.updateById(externalPatentMonitorSubject));
    }

    /**
     * 通过id删除监控主表（企业/技术汇总去重 + 统计）
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除监控主表（企业/技术汇总去重 + 统计）" , description = "通过id删除监控主表（企业/技术汇总去重 + 统计）" )
    @SysLog("通过id删除监控主表（企业/技术汇总去重 + 统计）" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(externalPatentMonitorSubjectService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param externalPatentMonitorSubject 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_externalPatentMonitorSubject_export')" )
    public List<ExternalPatentMonitorSubjectEntity> export(ExternalPatentMonitorSubjectEntity externalPatentMonitorSubject,Long[] ids) {
        return externalPatentMonitorSubjectService.list(Wrappers.lambdaQuery(externalPatentMonitorSubject).in(ArrayUtil.isNotEmpty(ids), ExternalPatentMonitorSubjectEntity::getId, ids));
    }
}