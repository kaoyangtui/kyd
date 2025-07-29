package com.pig4cloud.pigx.admin.dto.result;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "新增科研成果请求")
public class ResultCreateRequest {

    @Schema(description = "成果名称")
    private String name;

    @Schema(description = "所属学科")
    private String subject;

    @Schema(description = "技术领域")
    private List<String> techArea;

    @Schema(description = "研究方向")
    private String direction;

    @Schema(description = "是否依托项目，0否1是")
    private Integer fromProj;

    @Schema(description = "项目ID")
    private Long researchProjectId;

    @Schema(description = "成果简介")
    private String intro;

    @Schema(description = "技术成熟度")
    private String maturity;

    @Schema(description = "是否有实物，0否1是")
    private Integer hasObj;

    @Schema(description = "合作方式")
    private List<String> transWay;

    @Schema(description = "转化价格(万元)")
    private BigDecimal transPrice;

    @Schema(description = "成果评价或获奖情况")
    private String award;

    @Schema(description = "成果图片URL")
    private List<String> imgUrl;

    @Schema(description = "成果附件路径")
    private List<String> fileNames;

    @Schema(description = "完成人信息列表")
    private List<CompleterEntity> completers;
}
