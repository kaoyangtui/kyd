package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户业绩方案得分
 *
 * @author pigx
 * @date 2025-09-07 17:36:52
 */
@Data
@TenantTable
@TableName("t_perf_rule_result")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户业绩方案得分")
public class PerfRuleResultEntity extends Model<PerfRuleResultEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 方案ID快照
	*/
    @Schema(description="方案ID快照")
    private Long schemeId;
 
	/**
	* userId
	*/
    @Schema(description="userId")
    private Long userId;
 
	/**
	* userCode
	*/
    @Schema(description="userCode")
    private String userCode;
 
	/**
	* userName
	*/
    @Schema(description="userName")
    private String userName;
 
	/**
	* deptId
	*/
    @Schema(description="deptId")
    private Long deptId;
 
	/**
	* deptName
	*/
    @Schema(description="deptName")
    private String deptName;
 
	/**
	* ruleId
	*/
    @Schema(description="ruleId")
    private Long ruleId;
 
	/**
	* ipTypeCode
	*/
    @Schema(description="ipTypeCode")
    private String ipTypeCode;
 
	/**
	* ipTypeName
	*/
    @Schema(description="ipTypeName")
    private String ipTypeName;
 
	/**
	* ruleEventCode
	*/
    @Schema(description="ruleEventCode")
    private String ruleEventCode;
 
	/**
	* ruleEventName
	*/
    @Schema(description="ruleEventName")
    private String ruleEventName;

	/**
	* 件数(按业务主键去重，如PID)
	*/
    @Schema(description="件数(按业务主键去重，如PID)")
    private Integer eventCount;

	/**
	* 得分总和
	*/
    @Schema(description="得分总和")
    private BigDecimal scoreSum;

	/**
	* 最近一次事件时间(如授权公告日)
	*/
    @Schema(description="最近一次事件时间(如授权公告日)")
    private LocalDateTime lastEventTime;
 
	/**
	* createTime
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="createTime")
    private LocalDateTime createTime;
 
	/**
	* updateTime
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="updateTime")
    private LocalDateTime updateTime;
 
	/**
	* delFlag
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="delFlag")
    private String delFlag;
 
	/**
	* tenantId
	*/
    @Schema(description="tenantId")
    private Long tenantId;
}