package com.pig4cloud.pigx.admin.dto.icLayout;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "集成电路布图登记响应")
public class IcLayoutResponse extends BaseResponse {

    public static final String BIZ_CODE = "IC_LAYOUT";

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程 KEY")
    private String flowKey;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "流程当前节点")
    private String currentNodeName;

    @Schema(description = "集成电路布图名称")
    private String name;

    @Schema(description = "登记号")
    private String regNo;

    @Schema(description = "申请时间")
    private LocalDate applyDate;

    @Schema(description = "公告时间")
    private LocalDate publishDate;

    @Schema(description = "证书附件")
    private List<String> certFileUrl;

    @Schema(description = "校外创作人姓名")
    private String creatorOutName;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "完成人信息")
    private List<CompleterEntity> completers;

    @Schema(description = "权利人信息")
    private List<OwnerEntity> owners;
}