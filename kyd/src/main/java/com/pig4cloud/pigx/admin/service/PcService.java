package com.pig4cloud.pigx.admin.service;

import com.pig4cloud.pigx.admin.dto.pc.PortalStatisticResponse;

public interface PcService {

    PortalStatisticResponse getPortalStatistic();

    boolean increaseViewCount(String bizCode, Long bizId);

}
