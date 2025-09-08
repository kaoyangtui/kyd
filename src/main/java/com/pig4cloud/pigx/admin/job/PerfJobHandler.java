package com.pig4cloud.pigx.admin.job;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.dto.perf.PerfRuleCalcRequest;
import com.pig4cloud.pigx.admin.dto.perf.PerfRuleCalcSummary;
import com.pig4cloud.pigx.admin.entity.PerfSchemeEntity;
import com.pig4cloud.pigx.admin.service.PerfRuleResultService;
import com.pig4cloud.pigx.admin.service.PerfSchemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerfJobHandler {

    private final PerfSchemeService perfSchemeService;
    private final PerfRuleResultService perfRuleResultService;

    /**
     * 防止并发/重入
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 每天 02:00 执行：计算所有启用中的方案结果（使用方案自身的统计周期）
     * 注意：需要在应用上启用 @EnableScheduling
     */
    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Shanghai")
    public void runAllSchemesDaily() {
        if (!running.compareAndSet(false, true)) {
            log.warn("[PerfJob] already running, skip this tick");
            return;
        }
        long t0 = System.currentTimeMillis();
        try {
            List<PerfSchemeEntity> schemes = perfSchemeService.lambdaQuery()
                    .eq(PerfSchemeEntity::getStatus, 1)
                    .list();
            if (CollUtil.isEmpty(schemes)) {
                log.info("[PerfJob] no active schemes to run.");
                return;
            }

            int ok = 0, fail = 0;
            for (PerfSchemeEntity s : schemes) {
                try {
                    PerfRuleCalcRequest req = new PerfRuleCalcRequest();
                    req.setSchemeId(s.getId());
                    // periodStart / periodEnd 置空，runCalc 内部会使用方案配置的周期
                    req.setDryRun(false);

                    PerfRuleCalcSummary sum = perfRuleResultService.runCalc(req);
                    ok++;
                    log.info("[PerfJob] schemeId={} name='{}' done: events={}, rows={}, totalScore={}, period=[{}~{}]",
                            s.getId(), s.getSchemeName(),
                            sum.getEventTotal(), sum.getResultRows(), sum.getTotalScore(),
                            sum.getPeriodStart(), sum.getPeriodEnd());
                } catch (Exception e) {
                    fail++;
                    log.error("[PerfJob] schemeId={} name='{}' failed", s.getId(), s.getSchemeName(), e);
                }
            }
            log.info("[PerfJob] finished. ok={}, fail={}, cost={}ms",
                    ok, fail, System.currentTimeMillis() - t0);
        } finally {
            running.set(false);
        }
    }
}
