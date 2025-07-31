package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SupplyDemandMatchResultEntity;

public interface SupplyDemandMatchResultService extends IService<SupplyDemandMatchResultEntity> {

    SupplyDemandMatchResultEntity match(String demandType,
                                        String demandCode,
                                        String demandContent,
                                        String supplyType,
                                        String supplyCode,
                                        String supplyContent
    );
}