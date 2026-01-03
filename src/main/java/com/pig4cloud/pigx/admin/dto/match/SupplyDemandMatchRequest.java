package com.pig4cloud.pigx.admin.dto.match;

import lombok.Data;

/**
 * 供需匹配批量请求
 */
@Data
public class SupplyDemandMatchRequest {

    private String demandType;
    private Long demandId;
    private String demandContent;
    private String supplyType;
    private Long supplyId;
    private String supplyContent;
}
