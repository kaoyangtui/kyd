package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学科维度表
 *
 * @author pigx
 * @date 2025-05-30 08:00:17
 */
@Data
@TableName("t_dim_major")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "学科维度表")
public class DimMajorEntity extends Model<DimMajorEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Integer id;

    /**
     * parentId
     */
    @Schema(description = "parentId")
    private Integer parentId;

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
}