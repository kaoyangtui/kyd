package com.pig4cloud.pigx.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用响应字段基类，所有 Response DTO 可继承此类
 * 仅包含系统元数据字段（创建人、时间等）
 *
 * @author zhaoliang
 */
@Data
public class BaseResponse {

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人账号")
    private String createBy;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人姓名")
    private String createUserName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属部门名称")
    private String deptName;
}
