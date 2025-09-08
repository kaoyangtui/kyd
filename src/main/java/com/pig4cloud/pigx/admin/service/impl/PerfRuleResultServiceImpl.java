package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.IpTypeEnum;
import com.pig4cloud.pigx.admin.constants.RuleEventEnum;
import com.pig4cloud.pigx.admin.dto.perf.*;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.entity.PerfRuleResultEntity;
import com.pig4cloud.pigx.admin.entity.PerfSchemeEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PerfRuleResultMapper;
import com.pig4cloud.pigx.admin.service.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业绩计算 - 结果聚合与落库
 *
 * <p>职责定位：</p>
 * <ul>
 *   <li>按方案读取启用中的规则（仅做“流程编排”，不负责规则细节）</li>
 *   <li>调用“事件拉取（fetchEventsForRule）”以取得周期内满足规则的业务事件</li>
 *   <li>对事件进行“分配（splitShares）”，得到每个参与人的分配比例</li>
 *   <li>在内存中将同一方案下“用户 × 规则 × 部门”聚合为一行（件数=去重PID）</li>
 *   <li>持久化到 t_perf_rule_result：同一方案先清后写，保持最新汇总</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PerfRuleResultServiceImpl
        extends ServiceImpl<PerfRuleResultMapper, PerfRuleResultEntity>
        implements PerfRuleResultService {

    private final PerfSchemeService perfSchemeService;
    private final PerfRuleService perfRuleService;
    private final PatentInfoService patentInfoService;
    private final SoftCopyService softCopyService;
    private final StandardService standardService;
    private final PlantVarietyService plantVarietyService;
    private final IcLayoutService icLayoutService;
    private final IpAssignService ipAssignService;

    /**
     * 执行计算并（可选）落库。
     *
     * <p>总体流程：</p>
     * <ol>
     *   <li>校验并解析方案与统计周期</li>
     *   <li>加载方案内启用规则</li>
     *   <li>按规则拉取事件 → 计算分配 → 内存聚合</li>
     *   <li>非 dryRun 时清除该方案旧结果并批量写入</li>
     * </ol>
     *
     * @param req 计算请求（方案ID必填；周期为空则使用方案配置；dryRun=true时不落库）
     * @return 计算汇总（事件条数、落库行数、总分）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @SneakyThrows
    public PerfRuleCalcSummary runCalc(PerfRuleCalcRequest req) {
        // 1) 基础校验
        if (req == null || req.getSchemeId() == null) {
            throw new BizException("方案ID不能为空");
        }

        // 2) 读取方案与周期（周期为空时沿用方案配置）
        PerfSchemeEntity scheme = perfSchemeService.getById(req.getSchemeId());
        if (scheme == null) {
            throw new BizException("方案不存在");
        }

        LocalDate start = req.getPeriodStart() != null ? req.getPeriodStart() : scheme.getPeriodStart();
        LocalDate end = req.getPeriodEnd() != null ? req.getPeriodEnd() : scheme.getPeriodEnd();
        if (start != null && end != null && end.isBefore(start)) {
            throw new BizException("统计期结束早于开始时间");
        }

        // 3) 加载启用中的规则（只做流程控制，不做规则细节）
        List<PerfRuleEntity> rules = perfRuleService.lambdaQuery()
                .eq(PerfRuleEntity::getSchemeId, scheme.getId())
                .eq(PerfRuleEntity::getStatus, 1)
                .list();

        // 为便于后续按“ipTypeCode|ruleEventCode”快速定位规则，构建索引（如需引用规则附加配置可用到）
        Map<String, PerfRuleEntity> ruleIndex = rules.stream().collect(Collectors.toMap(
                r -> r.getIpTypeCode() + "|" + r.getRuleEventCode(),
                r -> r,
                // 若存在重复键则保留第一条
                (a, b) -> a
        ));

        // 4) 逐规则拉事件 → 分配 → 聚合
        Map<String, Acc> accMap = new HashMap<>();
        int eventTotal = 0;
        BigDecimal totalScore = BigDecimal.ZERO;

        for (PerfRuleEntity rule : rules) {
            // 4.1 拉取周期内满足该规则的业务事件（你在方法里完成）
            List<PerfEventDTO> events = fetchEventsForRule(rule, start, end);
            if (events == null || events.isEmpty()) {
                continue;
            }

            for (PerfEventDTO ev : events) {
                eventTotal++;

                // 4.2 基础分：事件可自带；否则使用规则分值；为0/负数则跳过
                BigDecimal base = ev.getBaseScore() != null ? ev.getBaseScore() :
                        (rule.getScore() != null ? rule.getScore() : BigDecimal.ZERO);
                if (base.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                // 4.3 计算分配比例（默认等分；可扩展为顺位加权/负责人加权等）
                List<PerfShareDTO> shares = splitShares(ev, rule, ruleIndex);
                if (shares == null || shares.isEmpty()) {
                    continue;
                }

                // 4.4 将每位参与人的得分累加到“用户 × 规则 × 部门”的聚合器
                for (PerfShareDTO s : shares) {
                    BigDecimal piece = base.multiply(s.getRatio() != null ? s.getRatio() : BigDecimal.ZERO);
                    totalScore = totalScore.add(piece);

                    PerfParticipantDTO p = s.getParticipant();
                    // key 维度：方案 + 用户 + 部门 + IP类型 + 事件
                    String key = key(scheme.getId(), p.getUserCode(), p.getDeptId(),
                            rule.getIpTypeCode(), rule.getRuleEventCode());

                    Acc a = accMap.computeIfAbsent(key, k -> new Acc(scheme.getId(), p, rule));
                    a.add(ev.getPid(), piece, ev.getEventTime());
                }
            }
        }

        // 5) 聚合结果转为实体，准备落库
        List<PerfRuleResultEntity> rows = accMap.values().stream()
                .map(Acc::toEntity)
                .collect(Collectors.toList());

        // 6) 非 dryRun：同一方案先清旧数据再批量写入
        boolean dryRun = Boolean.TRUE.equals(req.getDryRun());
        if (!dryRun) {
            this.lambdaUpdate()
                    .eq(PerfRuleResultEntity::getSchemeId, scheme.getId())
                    .remove();
            if (!rows.isEmpty()) {
                // 批量保存，默认 batchSize=1000，可按数据量微调
                this.saveBatch(rows, 1000);
            }
        }

        // 7) 返回执行摘要
        return PerfRuleCalcSummary.builder()
                .schemeId(scheme.getId())
                .periodStart(start)
                .periodEnd(end)
                .eventTotal(eventTotal)
                .resultRows(rows.size())
                .totalScore(scale4(totalScore))
                .build();
    }

    /**
     * 生成聚合键：同一方案下“用户 × 规则 × 部门”唯一。
     * <p>说明：如果你的业务允许同一人跨多部门统计且需要保留，则把 deptId 也纳入唯一键（当前实现已包含）。</p>
     */
    private String key(Long schemeId, String userCode, Long deptId, String ipType, String evt) {
        return schemeId + "|" + userCode + "|" + (deptId == null ? "" : deptId) + "|" + ipType + "|" + evt;
    }

    /**
     * 统一小数位处理：四舍五入到 4 位小数。
     */
    private BigDecimal scale4(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v.setScale(4, RoundingMode.HALF_UP);
    }

    /**
     * 事件拉取（扩展点）。
     *
     * <p>你需要在这里基于 {@code rule.getIpTypeCode()} 与 {@code rule.getRuleEventCode()}，
     * 并结合周期 {@code [start, end]}，从业务库查询符合条件的事件，组装为 {@link PerfEventDTO} 列表。</p>
     *
     * <p>示例思路：</p>
     * <ul>
     *   <li>发明专利授权：从“专利授权公告表”按时间查询，构建 participants 为发明人列表</li>
     *   <li>软著登记：从“软著证书表”按日期查询，participants 为作者/登记人</li>
     *   <li>专利转化：从“转化合同表”按生效时间查询，participants 为项目参与人</li>
     * </ul>
     *
     * @return 事件列表，允许为空
     */
    protected List<PerfEventDTO> fetchEventsForRule(PerfRuleEntity rule,
                                                    LocalDate start,
                                                    LocalDate end) {
        IpTypeEnum ipType = IpTypeEnum.fromCode(rule.getIpTypeCode());
        if (ipType == null) {
            throw new UnsupportedOperationException("不支持的知识产权类型：" + rule.getIpTypeCode());
        }

        // 规则事件编码可为空：部分类型（如转化细分）仅靠类型就能判定
        RuleEventEnum evt = RuleEventEnum.fromCode(rule.getRuleEventCode());

        switch (ipType) {
            // —— 专利三类统一从“专利域”取数
            case INVENTION, UTILITY_MODEL, DESIGN -> {
                return fetchPatentEvents(ipType, evt, rule, start, end);
            }

            // —— 软件著作权
            case SOFTWARE -> {
                return softCopyService.fetchSoftwareEvents(evt, rule, start, end);
            }

            // —— 标准
            case STANDARD -> {
                return standardService.fetchStandardEvents(evt, rule, start, end);
            }

            // —— 植物新品种
            case PLANT_VARIETY -> {
                return plantVarietyService.fetchPlantVarietyEvents(evt, rule, start, end);
            }

            // —— 集成电路布图
            case IC_LAYOUT -> {
                return icLayoutService.fetchIcLayoutEvents(evt, rule, start, end);
            }

            // —— 赋权
            case EMPOWER -> {
                return ipAssignService.fetchEmpowerEvents(evt, rule, start, end);
            }

            // —— 转化四细类（直接按类型分发）
            case TRANS_OPEN_LICENSE_PUBLISH -> {
                return fetchTransformOpenLicensePublish(rule, start, end);
            }
            case TRANS_OPEN_LICENSE_DEAL -> {
                return fetchTransformOpenLicenseDeal(rule, start, end);
            }
            case TRANS_TRANSFER -> {
                return fetchTransformTransfer(rule, start, end);
            }
            case TRANS_EQUITY -> {
                return fetchTransformEquity(rule, start, end);
            }

            // —— 旧版总类（若仍存在）
            case TRANSFORM -> {
                // 若保留旧“总类”，可根据 ruleEventCode 再细分；也可直接返回空，避免重复统计
                return Collections.emptyList();
            }

            default -> throw new UnsupportedOperationException("不支持的知识产权类型：" + ipType.name());
        }
    }

    /**
     * 专利域统一入口：根据事件枚举再分发
     * 建议：APPLY_PUB(申请公开)、GRANT_PUB(授权公告)、PRE_EXAM(预审)
     */
    private List<PerfEventDTO> fetchPatentEvents(IpTypeEnum patentType,
                                                 RuleEventEnum evt,
                                                 PerfRuleEntity rule,
                                                 LocalDate start,
                                                 LocalDate end) {
        if (evt == null) {
            log.warn("专利规则缺少事件编码，已忽略。type={}, ruleId={}", patentType, rule.getId());
            return Collections.emptyList();
        }
        return switch (evt) {
            case APPLY_PUB -> patentInfoService.fetchPatentApplyPub(patentType, rule, start, end);
            case GRANT_PUB -> patentInfoService.fetchPatentGrantPub(patentType, rule, start, end);
            default -> {
                log.warn("专利类型不支持的事件：type={}, event={}", patentType, evt);
                yield Collections.emptyList();
            }
        };
    }

// ===================== 转化四细类 =====================

    /**
     * 转化：开放许可发布
     * 建议表：open_license_publish，字段：pid, license_no, publish_time, participants
     */
    private List<PerfEventDTO> fetchTransformOpenLicensePublish(PerfRuleEntity rule,
                                                                LocalDate start,
                                                                LocalDate end) {
        // TODO 查询
        // List<TransformRecord> list = transformService.listOpenLicensePublish(start, end);

        List<PerfEventDTO> out = new ArrayList<>();
        // for (TransformRecord r : list) {
        //     List<PerfParticipantDTO> ps = loadTransformParticipants(r);
        //     out.add(buildEvent(
        //             r.getPid(),
        //             toDateTime(r.getPublishTime()),
        //             IpTypeEnum.TRANS_OPEN_LICENSE_PUBLISH.getCode(), IpTypeEnum.TRANS_OPEN_LICENSE_PUBLISH.getName(),
        //             RuleEventEnum.TRANS_LICENSE_ALLOW.getCode(), RuleEventEnum.TRANS_LICENSE_ALLOW.getName(),
        //             rule.getScore(),
        //             ps
        //     ));
        // }
        return out;
    }

    /**
     * 转化：开放许可达成
     * 建议表：open_license_deal，字段：pid, deal_time, amount
     */
    private List<PerfEventDTO> fetchTransformOpenLicenseDeal(PerfRuleEntity rule,
                                                             LocalDate start,
                                                             LocalDate end) {
        // TODO 查询
        return Collections.emptyList();
    }

    /**
     * 转化：转让
     * 建议表：assignment_deal，字段：pid, pay_time, amount
     */
    private List<PerfEventDTO> fetchTransformTransfer(PerfRuleEntity rule,
                                                      LocalDate start,
                                                      LocalDate end) {
        // TODO 查询
        return Collections.emptyList();
    }

    /**
     * 转化：作价入股
     * 建议表：equity_deal，字段：pid, effective_time, equity_value
     */
    private List<PerfEventDTO> fetchTransformEquity(PerfRuleEntity rule,
                                                    LocalDate start,
                                                    LocalDate end) {
        // TODO 查询
        return Collections.emptyList();
    }

// ===================== 通用构建器与工具方法 =====================

    /**
     * 构建一个事件对象；若传入 baseScore 为空，上层会在计算处回落到规则分值
     */
    private PerfEventDTO buildEvent(String pid,
                                    LocalDateTime eventTime,
                                    String ipTypeCode, String ipTypeName,
                                    String ruleEventCode, String ruleEventName,
                                    BigDecimal baseScore,
                                    List<PerfParticipantDTO> participants) {
        // 统一做一次时间过滤，防止上下游数据轻微越界
        if (!withinRange(eventTime, null, null)) {
            // 如果你希望严格用 [start,end] 过滤，请把 start/end 作为参数传入并调用 withinRange(eventTime, start, end)
        }
        return PerfEventDTO.builder()
                .pid(pid)
                .eventTime(eventTime)
                .ipTypeCode(ipTypeCode)
                .ipTypeName(ipTypeName)
                .ruleEventCode(ruleEventCode)
                .ruleEventName(ruleEventName)
                .baseScore(baseScore)
                .participants(participants == null ? Collections.emptyList() : participants)
                .build();
    }

    /**
     * 是否在 [start, end]（闭区间）内；任何一端为 null 则不限制
     */
    private boolean withinRange(LocalDateTime t, LocalDate start, LocalDate end) {
        if (t == null) {
            return false;
        }
        if (start != null && t.isBefore(start.atStartOfDay())) {
            return false;
        }
        if (end != null && t.isAfter(end.plusDays(1).atStartOfDay().minusNanos(1))) {
            return false;
        }
        return true;
    }

    /**
     * LocalDate 转 LocalDateTime（置为当天 00:00:00）
     */
    private LocalDateTime toDateTime(LocalDate d) {
        return d == null ? null : d.atStartOfDay();
    }

    /**
     * 转化类参与人装配占位（如果参与人不是基于 PID 的发明人，而是项目参与人，请用对应表）
     */
    private List<PerfParticipantDTO> loadTransformParticipants(Object record) {
        // TODO 根据你的转化数据结构从合同/项目成员表装配人员
        return Collections.emptyList();
    }


    /**
     * 分配策略（扩展点）。
     *
     * <p>默认实现：等分。你可以在这里根据“顺位、是否负责人、贡献度”等自定义计算 ratio。</p>
     *
     * <p>注意：</p>
     * <ul>
     *   <li>ratio 为 0~1 的小数，所有参与人的 ratio 之和不必强制等于 1（允许有权重系数），但通常会归一化。</li>
     *   <li>若想按顺位表加权，可先计算 rawWeight 后总和归一：ratio = rawWeight / sum(rawWeight)。</li>
     * </ul>
     */
    protected List<PerfShareDTO> splitShares(PerfEventDTO ev,
                                             PerfRuleEntity rule,
                                             Map<String, PerfRuleEntity> ruleIndex) {
        if (ev.getParticipants() == null || ev.getParticipants().isEmpty()) {
            return Collections.emptyList();
        }

        // 默认：等分
        BigDecimal n = new BigDecimal(ev.getParticipants().size());
        BigDecimal ratio = BigDecimal.ONE.divide(n, 8, RoundingMode.HALF_UP);

        return ev.getParticipants().stream()
                .map(p -> PerfShareDTO.builder().participant(p).ratio(ratio).build())
                .collect(Collectors.toList());
    }


    /**
     * 内存聚合器：把“事件 × 分配后的得分”累积到“用户 × 规则 × 部门”的维度。
     *
     * <p>字段说明：</p>
     * <ul>
     *   <li>pidSet：用于件数去重（COUNT DISTINCT pid）</li>
     *   <li>scoreSum：累计得分（按事件分配后的 piece 累加）</li>
     *   <li>lastEventTime：记录最近事件时间，用于结果表展示</li>
     * </ul>
     */
    @Getter
    private static class Acc {
        private final Long schemeId;
        private final Long userId;
        private final String userCode;
        private final String userName;
        private final Long deptId;
        private final String deptName;
        private final Long ruleId;
        private final String ipTypeCode;
        private final String ipTypeName;
        private final String ruleEventCode;
        private final String ruleEventName;

        private final Set<String> pidSet = new HashSet<>();
        private BigDecimal scoreSum = BigDecimal.ZERO;
        private LocalDateTime lastEventTime;

        /**
         * 使用参与人与规则初始化聚合维度
         */
        private Acc(Long schemeId, PerfParticipantDTO p, PerfRuleEntity r) {
            this.schemeId = schemeId;
            this.userId = p.getUserId();
            this.userCode = p.getUserCode();
            this.userName = p.getUserName();
            this.deptId = p.getDeptId();
            this.deptName = p.getDeptName();
            this.ruleId = r.getId();
            this.ipTypeCode = r.getIpTypeCode();
            this.ipTypeName = r.getIpTypeName();
            this.ruleEventCode = r.getRuleEventCode();
            this.ruleEventName = r.getRuleEventName();
        }

        /**
         * 累加一条事件贡献：
         * <ul>
         *   <li>件数采用 {@code pid} 去重</li>
         *   <li>分数累加 piece</li>
         *   <li>最近事件时间取最大值</li>
         * </ul>
         */
        private void add(String pid, BigDecimal piece, LocalDateTime time) {
            if (pid != null && !pid.isEmpty()) {
                pidSet.add(pid);
            }
            this.scoreSum = this.scoreSum.add(piece != null ? piece : BigDecimal.ZERO);
            if (time != null && (lastEventTime == null || time.isAfter(lastEventTime))) {
                lastEventTime = time;
            }
        }

        /**
         * 转换为持久化实体（落库 t_perf_rule_result）
         */
        private PerfRuleResultEntity toEntity() {
            PerfRuleResultEntity e = new PerfRuleResultEntity();
            e.setSchemeId(schemeId);
            e.setUserId(userId);
            e.setUserCode(userCode);
            e.setUserName(userName);
            e.setDeptId(deptId);
            e.setDeptName(deptName);
            e.setRuleId(ruleId);
            e.setIpTypeCode(ipTypeCode);
            e.setIpTypeName(ipTypeName);
            e.setRuleEventCode(ruleEventCode);
            e.setRuleEventName(ruleEventName);
            e.setEventCount(pidSet.size());
            e.setScoreSum(scoreSum == null ? BigDecimal.ZERO : scoreSum.setScale(4, RoundingMode.HALF_UP));
            e.setLastEventTime(lastEventTime);
            return e;
        }
    }
}
