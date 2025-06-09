package com.pig4cloud.pigx.admin.dto.patent.cnipr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "法律信息DTO")
public class Legal {

    @Schema(description = "最新")
    private String newInfo;

    @Schema(description = "公告日")
    private String prsDate;

    @Schema(description = "状态")
    private String prsCode;

    @Schema(description = "代码")
    private String strLegalCode;

    @Schema(description = "详细")
    private String codeExpl;
}
