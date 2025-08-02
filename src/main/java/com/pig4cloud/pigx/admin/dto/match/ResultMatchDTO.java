package com.pig4cloud.pigx.admin.dto.match;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-23 12:54:14
 */
@Data
@Schema(description = "科研成果表")
public class ResultMatchDTO {

    /**
     * 成果名称
     */
    @JSONField(name = "成果名称")
    @Schema(description = "成果名称")
    private String name;

    /**
     * 国民经济分类
     */
    @JSONField(name = "国民经济分类")
    @Schema(description = "国民经济分类")
    private String nec;

    /**
     * 成果简介
     */
    @JSONField(name = "成果简介")
    @Schema(description = "成果简介")
    private String intro;

    /**
     * 技术成熟度
     */
    @JSONField(name = "技术成熟度")
    @Schema(description = "技术成熟度")
    private String maturity;

    /**
     * 是否有实物，0否1是
     */
    @JSONField(name = "是否有实物，0否1是")
    @Schema(description = "是否有实物，0否1是")
    private Integer hasObj;

    /**
     * 标签
     */
    @JSONField(name = "标签")
    @Schema(description = "标签")
    private String tags;

}