package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.match.SupplyDemandMatchRequest;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;

import java.util.List;

public interface SupplyDemandMatchResultService extends IService<SupplyDemandMatchResultEntity> {

    SupplyDemandMatchResultEntity match(String demandType,
                                        Long demandId,
                                        String demandContent,
                                        String supplyType,
                                        Long supplyId,
                                        String supplyContent
    );

    List<SupplyDemandMatchResultEntity> matchBatch(List<SupplyDemandMatchRequest> requests);

    IPage<SupplyDemandMatchResultEntity> pageMatchByDemand(String demandType,
                                                           Long demandId,
                                                           String supplyType,
                                                           IPage page);

    IPage<SupplyDemandMatchResultEntity> pageMatchBySupply(String demandType,
                                                           Long supplyId,
                                                           String supplyType,
                                                           IPage page);

    long countDistinctSupplyByDemand(String demandType,
                                     Long demandId,
                                     String supplyType);

    long countDistinctDemandBySupply(String demandType,
                                     Long supplyId,
                                     String supplyType);

    List<SupplyDemandMatchResultEntity> getMatchByDemand(String demandType,
                                                       Long demandId,
                                                       String supplyType);

    List<SupplyDemandMatchResultEntity> getMatchBySupply(String demandType,
                                                         Long supplyId,
                                                         String supplyType);

    List<Long> getMatchId(String supplyType);
}
