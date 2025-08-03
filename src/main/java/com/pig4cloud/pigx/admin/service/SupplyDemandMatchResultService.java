package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    List<SupplyDemandMatchResultEntity> getMatchEntity(String demandType,
                                                       Long demandId,
                                                       String supplyType);

    List<Long> getMatchId(String supplyType);
}