package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "软著登记分页查询请求")
public class SoftCopyRegPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持登记号、软件名称模糊查询）")
    private String keyword;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "证书时间起（yyyy-MM-dd）")
    private String beginCertDate;

    @Schema(description = "证书时间止（yyyy-MM-dd）")
    private String endCertDate;
}
