package com.pig4cloud.pigx.admin.dto.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    // 流程修改补充字段
    @Schema(description = "是否备案（0否 1是）")
    private Integer hasRecord = 0;

    @Schema(description = "火炬中心备案文件")
    private List<String> recordFileUrl;

    @Schema(description = "确认转化金额（万元）")
    private BigDecimal transPrice = BigDecimal.ZERO;

    @Schema(description = "合同签订时间")
    private LocalDate contractSignTime;

    @Schema(description = "合同到期时间")
    private LocalDate contractExpireTime;

    @Schema(description = "专利转化合同文件URL")
    private List<String> contractFileUrl;

    @Schema(description = "专利转化收入奖励的申请文件URL")
    private List<String> rewardApplyFileUrl;

    @Schema(description = "专利转化收入分配方案文件URL")
    private List<String> allocationPlanFileUrl;

    @Schema(description = "专利转化收入分配方案")
    private List<IpTransformPlanVO> ipTransformPlanVOS;
}