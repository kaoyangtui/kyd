package com.pig4cloud.pigx.admin.dto.user;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "user分页查询请求")
public class UserPageRequest extends BasePageQuery {

    @Schema(description = "关键字(昵称/用户名/手机号)")
    private String keyword;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "省code")
    private String provinceCode;

    @Schema(description = "市code")
    private String cityCode;

    @Schema(description = "区code")
    private String districtCode;

}
