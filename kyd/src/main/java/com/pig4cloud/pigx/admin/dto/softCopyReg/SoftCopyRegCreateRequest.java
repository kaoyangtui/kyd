package com.pig4cloud.pigx.admin.dto.softCopyReg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 软著登记创建请求
 */
@Data
@Schema(description = "软著登记创建请求")
public class SoftCopyRegCreateRequest {

    @Schema(description = "著作权名称")
    private String name;

    @Schema(description = "登记号")
    private String regNo;

    @Schema(description = "证书号")
    private String certNo;

    @Schema(description = "证书时间")
    private String certDate;

    @Schema(description = "开发完成时间")
    private String devDate;

    @Schema(description = "首次发表时间")
    private String firstPubDate;

    @Schema(description = "关联软著提案ID")
    private Long relatedProposalId;

    @Schema(description = "证书文件URL")
    private String certFileUrl;

    @Schema(description = "著作权人列表")
    private List<SoftCopyRegOwnerRequest> owners;

    @Schema(description = "完成人列表")
    private List<SoftCopyRegCompleterRequest> completers;
}
