package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.plantVariety.*;
import com.pig4cloud.pigx.admin.entity.PlantVarietyEntity;

import java.util.List;

/**
 * 植物新品种权登记 Service 接口
 *
 * @author zhaoliang
 */
public interface PlantVarietyService {

    /**
     * 创建植物新品种权登记
     *
     * @param request 创建请求
     * @return 是否成功
     */
    Boolean create(PlantVarietyCreateRequest request);

    /**
     * 更新植物新品种权登记
     *
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean update(PlantVarietyUpdateRequest request);

    /**
     * 删除植物新品种权登记
     *
     * @param ids ID 列表
     * @return 是否成功
     */
    Boolean remove(List<Long> ids);

    /**
     * 获取详情
     *
     * @param id 主键
     * @return 返回信息
     */
    PlantVarietyResponse getDetail(Long id);

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param request 查询参数
     * @return 分页结果
     */
    IPage<PlantVarietyResponse> pageResult(Page<PlantVarietyEntity> page, PlantVarietyPageRequest request);
}
