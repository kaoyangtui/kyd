package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 国民经济表
 *
 * @author pigx
 * @date 2025-05-28 13:37:43
 */
@Data
@TableName("t_dim_ec")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "国民经济表")
public class DimEcEntity extends Model<DimEcEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Integer id;

    /**
     * code
     */
    @Schema(description = "code")
    private String code;

    /**
     * name
     */
    @Schema(description = "name")
    private String name;

    /**
     * level
     */
    @Schema(description = "level")
    private Integer level;

    /**
     * pCode
     */
    @Schema(description = "pCode")
    private String pCode;
}