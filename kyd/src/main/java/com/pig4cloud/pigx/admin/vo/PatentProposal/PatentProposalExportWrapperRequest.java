package com.pig4cloud.pigx.admin.vo.PatentProposal;

import com.pig4cloud.pigx.admin.vo.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利提案导出封装请求")
public class PatentProposalExportWrapperRequest extends ExportWrapperRequest<PatentProposalPageRequest> {

}
