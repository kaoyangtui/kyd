package com.pig4cloud.pigx.admin.dto.platformInfo;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "关于平台内容分页查询请求")
public class PlatformInfoPageRequest extends BasePageQuery {

    @Schema(description = "标题关键词")
    private String keyword;
}
