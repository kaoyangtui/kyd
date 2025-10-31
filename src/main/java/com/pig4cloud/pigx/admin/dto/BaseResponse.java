package com.pig4cloud.pigx.admin.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用响应字段基类，所有 Response DTO 可继承此类
 * 仅包含系统元数据字段（创建人、时间等）
 *
 * @author zhaoliang
 */
@Data
public class BaseResponse {

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人账号")
    private String createBy;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人姓名")
    private String createUserName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(
            description = "创建者展示，例如：李璠（1410 科技转化中心）",
            example = "李璠（1410 科技转化中心）",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getCreateUserDisplay() {
        // 主显示名优先顺序：createUserName > createBy > deptName
        String main = null;
        for (String s : new String[]{createUserName, createBy, deptName}) {
            if (StrUtil.isNotBlank(s)) {
                main = StrUtil.trim(s);
                break;
            }
        }
        if (main == null) {
            return null;
        }

        // 括号内附加信息：createBy、deptName（自动跳过空值）
        var extras = CollUtil.newArrayList(
                StrUtil.isNotBlank(createBy) ? StrUtil.trim(createBy) : null,
                StrUtil.isNotBlank(deptName) ? StrUtil.trim(deptName) : null
        );
        extras.removeIf(StrUtil::isBlank);

        if (extras.isEmpty()) {
            return main;
        }
        return StrUtil.format("{}（{}）", main, StrUtil.join(" ", extras));
    }

}
