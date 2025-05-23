package com.pig4cloud.pigx.admin.dto.standard;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "标准信息创建请求")
public class StandardCreateRequest {

    @Schema(description = "标准名称")
    private String name;

    @Schema(description = "标准号")
    private String code;

    @Schema(description = "主管部门")
    private String dept;

    @Schema(description = "标准类型")
    private String type;

    @Schema(description = "发布时间")
    private String pubDate;

    @Schema(description = "实施时间")
    private String implDate;

    @Schema(description = "校外起草人（多个用分号分隔）")
    private String drafterOutName;

    @Schema(description = "标准文本地址（多个用分号分隔）")
    private List<String> fileUrls;

    @Schema(description = "起草单位列表")
    private List<OwnerEntity> owners;

    @Schema(description = "完成人列表")
    private List<CompleterEntity> completers;
}