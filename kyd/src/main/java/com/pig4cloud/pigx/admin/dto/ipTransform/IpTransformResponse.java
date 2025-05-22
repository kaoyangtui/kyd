package com.pig4cloud.pigx.admin.dto.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 转化信息响应
 */
@Data
@Schema(description = "知识产权转化响应")
public class IpTransformResponse {

    @Schema(description = "主键 ID")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "转化项目名称")
    private String name;

    @Schema(description = "转化知识产权类型")
    private String ipType;

    @Schema(description = "知识产权编码（多个以分号分隔）")
    private List<String> ipCode;

    @Schema(description = "拟运用类型")
    private String useMode;

    @Schema(description = "拟运用价格（万元）")
    private BigDecimal usePrice;

    @Schema(description = "受让方")
    private String assignee;

    @Schema(description = "是否有关联关系")
    private Integer hasRelation;

    @Schema(description = "其它发明人同意证明附件URL")
    private List<String> consentFileUrl;

    @Schema(description = "专利转化承诺书附件URL")
    private List<String> promiseFileUrl;

    @Schema(description = "提交时间")
    private String createTime;

    @Schema(description = "所属院系")
    private String deptId;

    public static final String BIZ_CODE = "IP_TRANSFORM";
}