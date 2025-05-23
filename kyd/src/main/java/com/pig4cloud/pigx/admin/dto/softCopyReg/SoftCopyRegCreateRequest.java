package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "软著登记新增请求")
public class SoftCopyRegCreateRequest {

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

    @Schema(description = "完成人列表")
    private List<CompleterEntity> completers;

    @Schema(description = "著作权人列表")
    private List<OwnerEntity> owners;
}
