package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 知识产权赋权信息表
 *
 * @author pigx
 * @date 2025-05-20 09:38:08
 */
@Data
@TenantTable
@TableName("t_ip_assign")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "知识产权赋权信息表")
public class IpAssignEntity extends Model<IpAssignEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 业务编码
     */
    @Schema(description = "业务编码")
    private String code;

    /**
     * 流程实例 ID
     */
    @Schema(description = "流程实例 ID")
    private String flowInstId;

    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY")
    private String flowKey;

    /**
     * 流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回
     */
    @Schema(description = "流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    /**
     * 知识产权类型
     */
    @Schema(description = "知识产权类型")
    private String ipType;

    /**
     * 知识产权编码
     */
    @Schema(description = "知识产权编码")
    private String ipCode;

    /**
     * 被赋权人学号
     */
    @Schema(description = "被赋权人学号")
    private String assignToCode;

    /**
     * 被赋权人姓名
     */
    @Schema(description = "被赋权人姓名")
    private String assignToName;

    /**
     * 赋权类型
     */
    @Schema(description = "赋权类型")
    private String assignMode;

    /**
     * 其它发明人同意证明附件URL
     */
    @Schema(description = "其它发明人同意证明附件URL")
    private String proofFileUrl;

    /**
     * 赋权申请附件URL
     */
    @Schema(description = "赋权申请附件URL")
    private String attachFileUrl;

    /**
     * 所属组织ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "所属组织ID")
    private Long deptId;

    /**
     * 组织名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "组织名称")
    private String deptName;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识")
    private String delFlag;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private Long tenantId;
}