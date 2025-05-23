package com.pig4cloud.pigx.admin.dto.icLayout;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "集成电路布图登记响应")
public class IcLayoutResponse {

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

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "所属院系名称")
    private String deptName;

    @Schema(description = "完成人信息")
    private List<CompleterEntity> completers;

    @Schema(description = "权利人信息")
    private List<OwnerEntity> owners;
}