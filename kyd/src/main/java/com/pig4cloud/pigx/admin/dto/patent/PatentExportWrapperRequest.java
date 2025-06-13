package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利导出封装请求")
public class PatentExportWrapperRequest extends ExportWrapperRequest<PatentPageRequest> {

}