package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 科研团队导出封装请求
 */
@Data
@Schema(description = "科研团队导出封装请求")
public class ResearchTeamExportWrapperRequest extends ExportWrapperRequest<ResearchTeamPageRequest> {

}
