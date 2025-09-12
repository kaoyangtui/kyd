package com.pig4cloud.pigx.admin.dto.expert;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专家分页请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专家分页请求")
public class ExpertPageRequest extends BasePageQuery {

    @Schema(description = "关键词（姓名、工号、单位模糊）")
    private String keyword;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属院系")
    private Long deptId;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;
}
