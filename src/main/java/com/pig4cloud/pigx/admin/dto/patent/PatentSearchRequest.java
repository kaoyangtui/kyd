package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "专利检索请求")
public class PatentSearchRequest {

    @Schema(description = "检索关键词")
    private String keyword;

    @Schema(description = "专利类型")
    private List<String> patType;

    @Schema(description = "发明人")
    private String inventorCode;

    @Schema(description = "技术领域")
    private List<String> techArea;

    @Schema(description = "合作方式")
    private List<String> cooperationMode;

}
