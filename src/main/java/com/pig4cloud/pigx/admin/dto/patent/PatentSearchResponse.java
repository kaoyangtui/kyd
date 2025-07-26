package com.pig4cloud.pigx.admin.dto.patent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "专利检索响应")
public class PatentSearchResponse {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 封面图
     */
    @Schema(description = "封面图")
    private String cover;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String abs;

    /**
     * 专利类型
     */
    @Schema(description = "专利类型")
    private String patType;

    /**
     * 专利类型名称
     */
    @Schema(description = "专利类型名称")
    private String patTypeName;

    /**
     * 专利权状态
     */
    @Schema(description = "专利权状态")
    private String lprs;

    /**
     * 专利权状态代码
     */
    @Schema(description = "专利权状态代码")
    private String statusCode;

    /**
     * 申请号 (数组)
     */
    @Schema(description = "申请号 (数组)")
    private String appNumber;

    /**
     * 申请日
     */
    @Schema(description = "申请日")
    private String appDate;

    /**
     * 公开（公告）号 (数组)
     */
    @Schema(description = "公开（公告）号 (数组)")
    private String pubNumber;

    /**
     * 公开（公告）日
     */
    @Schema(description = "公开（公告）日")
    private String pubDate;

    /**
     * 授权日
     */
    @Schema(description = "授权日")
    private String grantDate;

    /**
     * 失效日
     */
    @Schema(description = "失效日")
    private String expireDate;

    /**
     * 申请（专利权）人 (List<String>)
     */
    @Schema(description = "申请（专利权）人 (List<String>)")
    private String applicantName;

    /**
     * 申请人类型 (Set<String>)
     */
    @Schema(description = "申请人类型 (Set<String>)")
    private String applicantType;

    /**
     * 发明（设计）人 (List<String>)
     */
    @Schema(description = "发明（设计）人 (List<String>)")
    private String inventorName;

    /**
     * 专利权人 (List<String>)
     */
    @Schema(description = "专利权人 (List<String>)")
    private String patentee;

    /**
     * 专利代理机构
     */
    @Schema(description = "专利代理机构")
    private String agencyName;

    /**
     * 代理人 (List<String>)
     */
    @Schema(description = "代理人 (List<String>)")
    private String agentName;

    /**
     * 关键词 (List<String>)
     */
    @Schema(description = "关键词 (List<String>)")
    private String patentWords;

    /**
     * 拟合作方式
     */
    @Schema(description = "拟合作方式")
    private String cooperationMode;

    /**
     * 拟交易金额，-1为面议
     */
    @Schema(description = "拟交易金额，-1为面议")
    private BigDecimal cooperationAmount;
}
