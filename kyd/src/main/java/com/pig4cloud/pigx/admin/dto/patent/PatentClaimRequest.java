package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 专利认领
 *
 * @author pigx
 * @date 2025-05-31 10:46:36
 */
@Data
@Schema(description = "专利认领")
public class PatentClaimRequest {

    /**
     * 专利PID
     */
    @Schema(description = "专利PID")
    private String pid;

    /**
     * 专利发明人
     */
    @Schema(description = "专利发明人")
    private List<PatentInventorVO> patentInventor;
}