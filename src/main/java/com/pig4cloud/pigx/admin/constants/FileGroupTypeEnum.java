package com.pig4cloud.pigx.admin.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "文件分组类型枚举")
public enum FileGroupTypeEnum {
    @Schema(description = "图片")
    IMAGE(10L, "图片"),
    @Schema(description = "视频")
    VIDEO(20L, "视频"),
    @Schema(description = "文件")
    FILE(30L, "文件");

    @EnumValue
    private final Long value;
    private final String desc;
}
