package com.pig4cloud.pigx.admin.dto.user;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "user导出包装请求")
public class UserExportWrapperRequest extends ExportWrapperRequest<UserPageRequest> {

}
