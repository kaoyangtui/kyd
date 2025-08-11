package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.baomidou.mybatisplus.annotation.TableId;
import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "软著登记返回信息")
public class SoftCopyRegResponse extends BaseResponse {

    public static final String BIZ_CODE = "SOFT_COPY";

    @TableId
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程 KEY")
    private String flowKey;

    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "著作权名称")
    private String name;

    @Schema(description = "登记号")
    private String regNo;

    @Schema(description = "证书号")
    private String certNo;

    @Schema(description = "证书时间")
    private LocalDate certDate;

    @Schema(description = "开发完成时间")
    private LocalDate devDate;

    @Schema(description = "首次发表时间")
    private LocalDate firstPubDate;

    @Schema(description = "关联软著提案 ID")
    private Long patentProposalId;

    @Schema(description = "软著证书附件 URL，多个用 ; 分隔")
    private List<String> certFileUrl;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "著作权人列表")
    private List<OwnerEntity> owners;

    @Schema(description = "完成人列表")
    private List<CompleterEntity> completers;
}
