package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 专利信息简要
 *
 * @author pigx
 * @date 2025-05-31 10:49:31
 */
@Schema(description = "专利信息简要")
@Data
public class PatentInfoSimpleVO {


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
     * 专利类型
     */
    @Schema(description = "专利类型")
    private String patType;

    /**
     * 专利权状态
     */
    @Schema(description = "专利权状态")
    private String lprs;

    /**
     * 当前法律状态
     * 公开、实审、未授权、有效、失效
     */
    @Schema(description = "当前法律状态")
    private String legalStatus;

    /**
     * 申请号
     */
    @Schema(description = "申请号")
    private String appNumber;

    /**
     * 申请日
     */
    @Schema(description = "申请日")
    private String appDate;

    /**
     * 公开（公告）号
     */
    @Schema(description = "公开（公告）号")
    private String pubNumber;

    /**
     * 公开（公告）日
     */
    @Schema(description = "公开（公告）日")
    private String pubDate;

    /**
     * 发明（设计）人
     */
    @Schema(description = "发明（设计）人")
    private String inventorName;

}