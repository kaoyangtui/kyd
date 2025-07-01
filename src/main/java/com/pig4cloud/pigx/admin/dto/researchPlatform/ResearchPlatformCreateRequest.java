package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "科研平台新增请求")
public class ResearchPlatformCreateRequest {

    @NotBlank(message = "科研平台名称不能为空")
    @Schema(description = "科研平台名称")
    private String name;

    @NotEmpty(message = "平台研究方向不能为空")
    @Schema(description = "平台研究方向；多个用;分隔")
    private List<String> direction;

    @NotBlank(message = "平台介绍不能为空")
    @Schema(description = "平台介绍")
    private String intro;

    @NotBlank(message = "联系人姓名不能为空")
    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @NotBlank(message = "负责人编码不能为空")
    @Schema(description = "负责人编码")
    private String leaderCode;

    @NotBlank(message = "负责人姓名不能为空")
    @Schema(description = "负责人姓名")
    private String leaderName;
}
