package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 知识产权转化信息表
 *
 * @author pigx
 * @date 2025-07-30 12:02:55
 */
@Data
@TenantTable
@TableName("t_ip_transform")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "知识产权转化信息表")
public class IpTransformEntity extends Model<IpTransformEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 业务编码
	*/
    @Schema(description="业务编码")
    private String code;

	/**
	* 流程实例 ID
	*/
    @Schema(description="流程实例 ID")
    private String flowInstId;

	/**
	* 流程KEY
	*/
    @Schema(description="流程KEY")
    private String flowKey;

	/**
	* 流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回
	*/
    @Schema(description="流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

	/**
	* 当前流程节点名称
	*/
    @Schema(description="当前流程节点名称")
    private String currentNodeName;

	/**
	* 转化项目名称
	*/
    @Schema(description="转化项目名称")
    private String name;

	/**
	* 转化知识产权类型
	*/
    @Schema(description="转化知识产权类型")
    private String ipType;

	/**
	* 知识产权编码，可多选使用;分隔
	*/
    @Schema(description="知识产权编码，可多选使用;分隔")
    private String ipCode;

	/**
	* 拟运用类型（开放许可、转让、作价入股）
	*/
    @Schema(description="拟运用类型（开放许可、转让、作价入股）")
    private String useMode;

	/**
	* 拟运用价格（万元）
	*/
    @Schema(description="拟运用价格（万元）")
    private BigDecimal usePrice;

	/**
	* 确认转化金额（万元）
	*/
    @Schema(description="确认转化金额（万元）")
    private BigDecimal transPrice;

	/**
	* 受让方
	*/
    @Schema(description="受让方")
    private String assignee;

	/**
	* 是否有关联关系（0否 1是）
	*/
    @Schema(description="是否有关联关系（0否 1是）")
    private Integer hasRelation;

	/**
	* 合同签订时间
	*/
    @Schema(description="合同签订时间")
    private LocalDate contractSignTime;

	/**
	* 合同到期时间
	*/
    @Schema(description="合同到期时间")
    private LocalDate contractExpireTime;

	/**
	* 其他发明人同意证明附件URL
	*/
    @Schema(description="其他发明人同意证明附件URL")
    private String consentFileUrl;

	/**
	* 专利转化承诺书附件URL
	*/
    @Schema(description="专利转化承诺书附件URL")
    private String promiseFileUrl;

	/**
	* 火炬中心备案文件
	*/
    @Schema(description="火炬中心备案文件")
    private String recordFileUrl;

	/**
	* 专利转化合同文件URL
	*/
    @Schema(description="专利转化合同文件URL")
    private String contractFileUrl;

	/**
	* 专利转化收入奖励的申请文件URL
	*/
    @Schema(description="专利转化收入奖励的申请文件URL")
    private String rewardApplyFileUrl;

	/**
	* 专利转化收入分配方案文件URL
	*/
    @Schema(description="专利转化收入分配方案文件URL")
    private String allocationPlanFileUrl;

	/**
	* 是否备案（0否 1是）
	*/
    @Schema(description="是否备案（0否 1是）")
    private Integer hasRecord;

	/**
	* 是否到款（0否 1是）
	*/
    @Schema(description="是否到款（0否 1是）")
    private Integer hasReceivedPayment;

	/**
	* 所属院系
	*/
    @Schema(description="所属院系")
    private String deptId;

	/**
	* 组织名称
	*/
    @Schema(description="组织名称")
    private String deptName;

	/**
	* 创建人ID
	*/
    @Schema(description="创建人ID")
    private Long createUserId;

	/**
	* 创建人姓名
	*/
    @Schema(description="创建人姓名")
    private String createUserName;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 更新人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新人")
    private String updateBy;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

	/**
	* 删除标识
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标识")
    private String delFlag;

	/**
	* 租户
	*/
    @Schema(description="租户")
    private Long tenantId;
}