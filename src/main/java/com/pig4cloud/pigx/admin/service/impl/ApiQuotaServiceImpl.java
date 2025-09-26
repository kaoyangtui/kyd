package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.QuotaPeriodType;
import com.pig4cloud.pigx.admin.entity.ApiQuotaEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ApiQuotaMapper;
import com.pig4cloud.pigx.admin.service.ApiQuotaService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiQuotaServiceImpl extends ServiceImpl<ApiQuotaMapper, ApiQuotaEntity> implements ApiQuotaService {

    @Override
    @SneakyThrows
    public void checkLimitOrThrow(String apiCode, Long userId, QuotaPeriodType periodType) {
        ApiQuotaEntity q = findBestScope(apiCode, userId, periodType);
        if (q == null || q.getStatus() == null || q.getStatus() != 1) {
            throw new BizException("接口未配置额度或未启用：{}", apiCode);
        }
        if (q.getUsedCount() != null && q.getLimitTotal() != null && q.getUsedCount() >= q.getLimitTotal()) {
            throw new BizException("额度已用完：{}", apiCode);
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void consumeOrThrow(String apiCode, Long userId, QuotaPeriodType periodType, int delta) {
        if (delta <= 0) return;
        ApiQuotaEntity q = findBestScope(apiCode, userId, periodType);
        if (q == null || q.getStatus() == null || q.getStatus() != 1) {
            throw new BizException("接口未配置额度或未启用：{}", apiCode);
        }
        UpdateWrapper<ApiQuotaEntity> uw = new UpdateWrapper<>();
        uw.eq("id", q.getId())
                .eq("status", 1)
                .apply("used_count + {0} <= limit_total", delta)
                .setSql("used_count = used_count + " + delta);
        int rows = this.baseMapper.update(null, uw);
        if (rows <= 0) {
            throw new BizException("额度已用完：{}", apiCode);
        }
    }

    @Override
    public int getRemain(String apiCode, Long userId, QuotaPeriodType periodType) {
        ApiQuotaEntity q = findBestScope(apiCode, userId, periodType);
        if (q == null || q.getStatus() == null || q.getStatus() != 1) return 0;
        Integer limit = q.getLimitTotal() == null ? 0 : q.getLimitTotal();
        Integer used = q.getUsedCount() == null ? 0 : q.getUsedCount();
        int remain = limit - used;
        return Math.max(remain, 0);
    }

    private ApiQuotaEntity findBestScope(String apiCode, Long userId, QuotaPeriodType periodType) {
        Date[] range = currentRange(periodType);
        LambdaQueryWrapper<ApiQuotaEntity> qw = new LambdaQueryWrapper<ApiQuotaEntity>()
                .eq(ApiQuotaEntity::getApiCode, apiCode)
                .eq(ApiQuotaEntity::getPeriodType, periodType.name())
                .le(ApiQuotaEntity::getPeriodStart, range[1])
                .ge(ApiQuotaEntity::getPeriodEnd, range[0])
                .eq(ApiQuotaEntity::getStatus, 1);
        List<ApiQuotaEntity> list = this.list(qw);
        if (CollUtil.isEmpty(list)) return null;

        ApiQuotaEntity userScoped = null;
        ApiQuotaEntity globalScoped = null;

        for (ApiQuotaEntity it : list) {
            boolean userMatch = ObjectUtil.equal(it.getUserId(), userId);
            if (userMatch) {
                userScoped = it;
                break;
            }
            if (it.getTenantId() == null && it.getUserId() == null) {
                globalScoped = it;
            }
        }
        if (userScoped != null) return userScoped;
        return globalScoped;
    }

    private Date[] currentRange(QuotaPeriodType type) {
        Date now = new Date();
        switch (type) {
            case DAILY:
                return new Date[]{DateUtil.beginOfDay(now), DateUtil.endOfDay(now)};
            case MONTHLY:
                return new Date[]{DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now)};
            default:
                return new Date[]{DateUtil.parse("1970-01-01 00:00:00"), DateUtil.parse("2099-12-31 23:59:59")};
        }
    }
}
