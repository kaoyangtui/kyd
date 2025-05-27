package com.pig4cloud.pigx.admin.dto.assetPolicy;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产政策分页查询请求")
public class AssetPolicyPageRequest extends BasePageQuery {

    @Schema(description = "关键词（标题/内容/供稿）")
    private String keyword;

    @Schema(description = "所属院系ID")
    private String deptId;

    @Schema(description = "提交时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "提交时间止（yyyy-MM-dd）")
    private String endTime;
}
