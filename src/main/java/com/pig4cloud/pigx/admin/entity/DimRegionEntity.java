package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域表
 *
 * @author pigx
 * @date 2025-05-28 14:23:20
 */
@Data
@TableName("t_dim_region")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "区域表")
public class DimRegionEntity extends Model<DimRegionEntity> {


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
     * cname
     */
    @Schema(description = "cname")
    private String cname;

    /**
     * ctype
     */
    @Schema(description = "ctype")
    private Integer ctype;
}