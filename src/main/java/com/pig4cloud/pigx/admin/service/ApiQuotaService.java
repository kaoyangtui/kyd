package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.constants.QuotaPeriodType;
import com.pig4cloud.pigx.admin.entity.ApiQuotaEntity;

public interface ApiQuotaService extends IService<ApiQuotaEntity> {
    void checkLimitOrThrow(String apiCode, Long userId, QuotaPeriodType periodType);

    void consumeOrThrow(String apiCode, Long userId, QuotaPeriodType periodType, int delta);

    int getRemain(String apiCode, Long userId, QuotaPeriodType periodType);
}