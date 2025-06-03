package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pigx.admin.dto.pc.PortalStatisticResponse;
import com.pig4cloud.pigx.admin.entity.DemandEntity;
import com.pig4cloud.pigx.admin.entity.ExpertEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.ObjectUtil;

@Service
@RequiredArgsConstructor
public class PortalStatisticServiceImpl implements PortalStatisticService {

    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final ExpertService expertService;

    @Override
    public PortalStatisticResponse getPortalStatistic() {
        PortalStatisticResponse resp = new PortalStatisticResponse();
        resp.setPatentCount(
                ObjectUtil.defaultIfNull(
                        patentInfoService.count(new LambdaQueryWrapper<PatentInfoEntity>().eq(PatentInfoEntity::getDelFlag, "0")),
                        0L
                )
        );
        resp.setResultCount(
                ObjectUtil.defaultIfNull(
                        resultService.count(new LambdaQueryWrapper<ResultEntity>().eq(ResultEntity::getShelfStatus, 1)),
                        0L
                )
        );
        resp.setDemandCount(
                ObjectUtil.defaultIfNull(
                        demandService.count(new LambdaQueryWrapper<DemandEntity>().eq(DemandEntity::getDelFlag, "0")),
                        0L
                )
        );
        resp.setExpertCount(
                ObjectUtil.defaultIfNull(
                        expertService.count(new LambdaQueryWrapper<ExpertEntity>().eq(ExpertEntity::getShelfStatus, 1)),
                        0L
                )
        );
        return resp;
    }
}
