package com.pig4cloud.pigx.admin.dto.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建请求
 */
@Data
@Schema(description = "创建知识产权转化申请")
public class IpTransformCreateRequest {

    @Schema(description = "转化项目名称")
    private String name;

    @Schema(description = "转化知识产权类型")
    private String ipType;

    @Schema(description = "知识产权编码，多个以分号分隔")
    private List<String> ipCode;

    @Schema(description = "拟运用类型")
    private String useMode;

    @Schema(description = "拟运用价格（万元）")
    private BigDecimal usePrice;

    @Schema(description = "受让方")
    private String assignee;

    @Schema(description = "是否有关联关系（0否 1是）")
    private Integer hasRelation;

    @Schema(description = "其它发明人同意证明附件URL，多文件用分号分隔")
    private List<String> consentFileUrl;

    @Schema(description = "专利转化承诺书附件URL，多文件用分号分隔")
    private List<String> promiseFileUrl;
}