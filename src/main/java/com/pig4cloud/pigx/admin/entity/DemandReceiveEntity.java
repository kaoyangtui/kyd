package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业需求接收表
 *
 * @author pigx
 * @date 2025-09-23 11:19:11
 */
@Data
@TableName("t_demand_receive")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求接收表")
public class DemandReceiveEntity extends Model<DemandReceiveEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

    /**
     * 关联需求ID
     */
    @Schema(description = "关联需求ID")
    private Long demandId;

    /**
     * 接收人ID
     */
    @Schema(description="接收人ID")
    private Long receiveUserId;

    @Schema(description = "已读（0否，1是）")
    private Integer readFlag;
}