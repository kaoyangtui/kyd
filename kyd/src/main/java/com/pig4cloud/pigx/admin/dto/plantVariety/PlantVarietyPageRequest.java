package com.pig4cloud.pigx.admin.dto.plantVariety;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "植物新品种权分页查询请求")
public class PlantVarietyPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持品种权号、品种名称模糊查询）")
    private String keyword;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "授权时间起（yyyy-MM-dd）")
    private String authBeginTime;

    @Schema(description = "授权时间止（yyyy-MM-dd）")
    private String authEndTime;
}