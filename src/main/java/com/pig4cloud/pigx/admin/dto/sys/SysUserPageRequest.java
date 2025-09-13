package com.pig4cloud.pigx.admin.dto.sys;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户分页查询请求")
public class SysUserPageRequest extends BasePageQuery {

    @Schema(description = "用户名（模糊）")
    private String username;

    @Schema(description = "手机号（模糊）")
    private String phone;

    @Schema(description = "姓名（模糊）")
    private String name;

    @Schema(description = "部门ID（精确）")
    private Long deptId;

    @Schema(description = "角色ID（精确）")
    private Long roleId;

    @Schema(description = "角色编码（模糊）")
    private String roleCode;

    @Schema(description = "角色名称（模糊）")
    private String roleName;

}
