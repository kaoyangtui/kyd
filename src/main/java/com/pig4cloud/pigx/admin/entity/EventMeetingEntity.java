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
 * 活动会议主表
 *
 * @author pigx
 * @date 2025-05-30 15:06:48
 */
@Data
@TenantTable
@TableName("t_event_meeting")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动会议主表")
public class EventMeetingEntity extends Model<EventMeetingEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址")
    private String imgUrl;

    /**
     * 活动名称
     */
    @Schema(description = "活动名称")
    private String name;

    /**
     * 举办方
     */
    @Schema(description = "举办方")
    private String organizer;

    /**
     * 活动时间
     */
    @Schema(description = "活动时间")
    private LocalDate eventTime;

    /**
     * 活动地点
     */
    @Schema(description = "活动地点")
    private String location;

    /**
     * 活动内容
     */
    @Schema(description = "活动内容")
    private String content;

    /**
     * 报名开始时间
     */
    @Schema(description = "报名开始时间")
    private LocalDate signUpStart;

    /**
     * 报名截止时间
     */
    @Schema(description = "报名截止时间")
    private LocalDate signUpEnd;

    @Schema(description = "附件URL，多文件用;分隔，DTO用List")
    private String fileUrl;

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
    @Schema(description = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人姓名")
    private String createUserName;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long viewCount;

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