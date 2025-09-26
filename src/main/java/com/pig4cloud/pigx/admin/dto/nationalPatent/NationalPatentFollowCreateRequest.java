package com.pig4cloud.pigx.admin.dto.nationalPatent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 全国专利-关注请求
 */
@Data
@Schema(description = "全国专利-关注请求")
public class NationalPatentFollowCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "专利PID")
    @NotBlank(message = "pid 不能为空")
    private String pid;

    @Schema(description = "备注（可选）", example = "与项目A相关")
    @Size(max = 255, message = "备注长度不能超过255")
    private String note;

    @Schema(description = "标签（可选；分号分隔）", example = "新能源;高价值")
    @Size(max = 255, message = "标签长度不能超过255")
    private String tags;
}
