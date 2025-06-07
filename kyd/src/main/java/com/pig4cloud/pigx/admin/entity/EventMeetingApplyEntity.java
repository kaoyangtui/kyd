package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动报名信息子表
 *
 * @author pigx
 * @date 2025-05-30 15:07:02
 */
@Data
@TenantTable
@TableName("t_event_meeting_apply")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动报名信息子表")
public class EventMeetingApplyEntity extends Model<EventMeetingApplyEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 关联活动ID
     */
    @Schema(description = "关联活动ID")
    private Long meetingId;

    /**
     * 报名时间
     */
    @Schema(description = "报名时间")
    private LocalDateTime applyTime;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 所属单位
     */
    @Schema(description = "所属单位")
    private String organization;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String phone;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

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
     * 创建/提交人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交人")
    private String createBy;

    /**
     * 创建/提交时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交时间")
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