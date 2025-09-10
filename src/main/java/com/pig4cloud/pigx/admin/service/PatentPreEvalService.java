package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.PatentPreEval.*;
import com.pig4cloud.pigx.admin.entity.PatentPreEvalEntity;

/**
 * 专利申请前评估 - Service
 * 说明：
 * - Controller 层只使用 Request/Response 命名；
 * - Service 层不封装 R，直接返回 DTO 或布尔；
 * - page/detail 为只读接口，create/update/updateResult/remove 为写接口。
 */
public interface PatentPreEvalService extends IService<PatentPreEvalEntity> {

    /**
     * 分页查询
     * @param page MyBatis-Plus 分页对象（由 PageUtil.toPage(PageRequest) 转换）
     * @param req  查询条件
     * @return 分页结果（Response DTO）
     */
    Page<PatentPreEvalResponse> pageResult(Page page, PatentPreEvalPageRequest req);

    /**
     * 详情
     * @param req 详情请求（支持 id 或 code）
     * @return 详情（Response DTO）
     */
    PatentPreEvalResponse detail(PatentPreEvalDetailRequest req);

    /**
     * 新增
     * @param req 创建请求
     * @return 是否成功
     */
    boolean create(PatentPreEvalCreateRequest req);

    /**
     * 修改基础信息
     * @param req 修改请求
     * @return 是否成功
     */
    boolean update(PatentPreEvalUpdateRequest req);

    /**
     * 修改评估结果字段
     * @param req 仅更新结果信息
     * @return 是否成功
     */
    boolean updateResult(PatentPreEvalUpdateResultRequest req);

    /**
     * 删除（逻辑删）
     * @param ids 待删除ID集合
     * @return 是否成功
     */
    boolean remove(java.util.List<Long> ids);
}
