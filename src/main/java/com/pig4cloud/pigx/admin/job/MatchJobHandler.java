package com.pig4cloud.pigx.admin.job;

import com.pig4cloud.pigx.admin.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchJobHandler {

    private final MatchService matchService;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 每天 03:00 执行供需匹配。
     * 注意：需要在应用上启用 @EnableScheduling
     */
    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Shanghai")
    public void runDailyMatch() {
        if (!running.compareAndSet(false, true)) {
            log.warn("[MatchJob] already running, skip this tick");
            return;
        }
        long t0 = System.currentTimeMillis();
        try {
            Boolean ok = matchService.demandMatch();
            log.info("[MatchJob] demandMatch finished: ok={}, cost={}ms",
                    ok, System.currentTimeMillis() - t0);
        } catch (Exception e) {
            log.error("[MatchJob] demandMatch failed", e);
        } finally {
            running.set(false);
        }
    }
}
