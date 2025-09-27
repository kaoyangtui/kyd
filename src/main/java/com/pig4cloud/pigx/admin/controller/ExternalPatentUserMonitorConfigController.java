package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.admin.entity.ExternalPatentUserMonitorConfigEntity;
import com.pig4cloud.pigx.admin.service.ExternalPatentUserMonitorConfigService;
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
 * 用户监控配置（企业/技术，单值）
 *
 * @author pigx
 * @date 2025-09-11 13:36:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/externalPatentUserMonitorConfig" )
@Tag(description = "externalPatentUserMonitorConfig" , name = "用户监控配置（企业/技术，单值）管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ExternalPatentUserMonitorConfigController {

    private final  ExternalPatentUserMonitorConfigService externalPatentUserMonitorConfigService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param externalPatentUserMonitorConfig 用户监控配置（企业/技术，单值）
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    public R getExternalPatentUserMonitorConfigPage(@ParameterObject Page page, @ParameterObject ExternalPatentUserMonitorConfigEntity externalPatentUserMonitorConfig) {
        LambdaQueryWrapper<ExternalPatentUserMonitorConfigEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(externalPatentUserMonitorConfigService.page(page, wrapper));
    }


    /**
     * 通过id查询用户监控配置（企业/技术，单值）
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(externalPatentUserMonitorConfigService.getById(id));
    }

    /**
     * 新增用户监控配置（企业/技术，单值）
     * @param externalPatentUserMonitorConfig 用户监控配置（企业/技术，单值）
     * @return R
     */
    @Operation(summary = "新增用户监控配置（企业/技术，单值）" , description = "新增用户监控配置（企业/技术，单值）" )
    @SysLog("新增用户监控配置（企业/技术，单值）" )
    @PostMapping
    public R save(@RequestBody ExternalPatentUserMonitorConfigEntity externalPatentUserMonitorConfig) {
        return R.ok(externalPatentUserMonitorConfigService.save(externalPatentUserMonitorConfig));
    }

    /**
     * 修改用户监控配置（企业/技术，单值）
     * @param externalPatentUserMonitorConfig 用户监控配置（企业/技术，单值）
     * @return R
     */
    @Operation(summary = "修改用户监控配置（企业/技术，单值）" , description = "修改用户监控配置（企业/技术，单值）" )
    @SysLog("修改用户监控配置（企业/技术，单值）" )
    @PutMapping
    public R updateById(@RequestBody ExternalPatentUserMonitorConfigEntity externalPatentUserMonitorConfig) {
        return R.ok(externalPatentUserMonitorConfigService.updateById(externalPatentUserMonitorConfig));
    }

    /**
     * 通过id删除用户监控配置（企业/技术，单值）
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除用户监控配置（企业/技术，单值）" , description = "通过id删除用户监控配置（企业/技术，单值）" )
    @SysLog("通过id删除用户监控配置（企业/技术，单值）" )
    @DeleteMapping
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(externalPatentUserMonitorConfigService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param externalPatentUserMonitorConfig 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    public List<ExternalPatentUserMonitorConfigEntity> export(ExternalPatentUserMonitorConfigEntity externalPatentUserMonitorConfig,Long[] ids) {
        return externalPatentUserMonitorConfigService.list(Wrappers.lambdaQuery(externalPatentUserMonitorConfig).in(ArrayUtil.isNotEmpty(ids), ExternalPatentUserMonitorConfigEntity::getId, ids));
    }
}