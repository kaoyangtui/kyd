package com.pig4cloud.pigx.admin.dto.plantVariety;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "植物新品种权登记响应")
public class PlantVarietyResponse {

    public static final String BIZ_CODE = "PLANT_VARIETY";

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程KEY")
    private String flowKey;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "品种名称")
    private String name;

    @Schema(description = "品种权号")
    private String rightNo;

    @Schema(description = "属或种")
    private String genusType;

    @Schema(description = "属或种名称")
    private String genusName;

    @Schema(description = "申请时间")
    private Date applyDate;

    @Schema(description = "授权时间")
    private Date authDate;

    @Schema(description = "校外培育人姓名")
    private String breederOutName;

    @Schema(description = "证书附件URL")
    private List<String> certFileUrl;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "所属组织")
    private Long deptId;

    @Schema(description = "组织名称")
    private String deptName;

    @Schema(description = "完成人列表")
    private List<CompleterEntity> completers;

    @Schema(description = "权利人列表")
    private List<OwnerEntity> owners;
}
