package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 大模型调用日志表
 *
 * @author pigx
 * @date 2025-07-31 15:47:16
 */
@Data
@TableName("t_model_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "大模型调用日志表")
public class ModelLogEntity extends Model<ModelLogEntity> {


	/**
	* 主键ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键ID")
    private Long id;

	/**
	* 编码
	*/
    @Schema(description="编码")
    private String code;

	/**
	* 用户ID
	*/
    @Schema(description="用户ID")
    private Long userId;

	/**
	* 模型类型
	*/
    @Schema(description="模型类型")
    private String modelType;

	/**
	* 业务名称
	*/
    @Schema(description="业务名称")
    private String bizName;

	/**
	* 输入内容
	*/
    @Schema(description="输入内容")
    private String inputContent;

	/**
	* 输出内容
	*/
    @Schema(description="输出内容")
    private String outputContent;

	/**
	* 推理内容
	*/
    @Schema(description="推理内容")
    private String inferenceContent;

	/**
	* 训练状态，处理中 TRAIN 成功 SUCCESS 失败 FAILED
	*/
    @Schema(description="训练状态，处理中 TRAIN 成功 SUCCESS 失败 FAILED")
    private String status;

	/**
	* 训练失败原因
	*/
    @Schema(description="训练失败原因")
    private String errorMessage;

	/**
	* 任务开始时间
	*/
    @Schema(description="任务开始时间")
    private LocalDateTime jobStartTime;

	/**
	* 任务结束时间
	*/
    @Schema(description="任务结束时间")
    private LocalDateTime jobEndTime;

	/**
	* 任务执行时间(秒)
	*/
    @Schema(description="任务执行时间(秒)")
    private Integer jobExeTime;

	/**
	* 输入量
	*/
    @Schema(description="输入量")
    private Long inputTokens;

	/**
	* 输出量
	*/
    @Schema(description="输出量")
    private Long outputTokens;
 
	/**
	* remark
	*/
    @Schema(description="remark")
    private String remark;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 删除标识 0-未删除 1-删除
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标识 0-未删除 1-删除")
    private String delFlag;
}