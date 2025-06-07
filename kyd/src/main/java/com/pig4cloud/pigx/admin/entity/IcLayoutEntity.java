package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 集成电路布图登记信息
 *
 * @author pigx
 * @date 2025-05-23 20:24:32
 */
@Data
@TenantTable
@TableName("t_ic_layout")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "集成电路布图登记信息")
public class IcLayoutEntity extends Model<IcLayoutEntity> {


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
     * 集成电路布图名称
     */
    @Schema(description = "集成电路布图名称")
    private String name;

    /**
     * 登记号
     */
    @Schema(description = "登记号")
    private String regNo;

    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDate applyDate;

    /**
     * 公告时间
     */
    @Schema(description = "公告时间")
    private LocalDate publishDate;

    /**
     * 集成电路布图证书URL
     */
    @Schema(description = "集成电路布图证书URL")
    private String certFileUrl;

    /**
     * 校外创作人姓名，多个用;分隔
     */
    @Schema(description = "校外创作人姓名，多个用;分隔")
    private String creatorOutName;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

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