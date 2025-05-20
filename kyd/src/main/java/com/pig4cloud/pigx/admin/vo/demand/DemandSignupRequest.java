package com.pig4cloud.pigx.admin.vo.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "企业需求报名请求")
public class DemandSignupRequest {

    @Schema(description = "关联需求ID", required = true)
    private Long demandId;

    @Schema(description = "报名人姓名", required = true)
    private String name;

    @Schema(description = "学院/院系名称", required = true)
    private String deptName;

    @Schema(description = "联系电话", required = true)
    private String phone;

    @Schema(description = "邮箱", required = true)
    private String email;

    @Schema(description = "备注")
    private String remark;
}
