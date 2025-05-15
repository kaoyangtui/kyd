package com.pig4cloud.pigx.admin.vo.IcLayout;

import com.pig4cloud.pigx.admin.vo.ExportExecute.ExportExecuteRequest;
import com.pig4cloud.pigx.admin.vo.ExportWrapperRequest;
import com.pig4cloud.pigx.admin.vo.PatentProposal.PatentProposalPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 集成电路布图导出包装请求
 *
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "集成电路布图导出包装请求")
public class IcLayoutExportWrapperRequest extends ExportWrapperRequest<IcLayoutPageRequest> {

}
