package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.perf.*;
import com.pig4cloud.pigx.admin.entity.PerfRuleEntity;
import com.pig4cloud.pigx.admin.entity.PerfSchemeEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PerfRuleResultMapper;
import com.pig4cloud.pigx.admin.mapper.PerfSchemeMapper;
import com.pig4cloud.pigx.admin.service.PerfRuleService;
import com.pig4cloud.pigx.admin.service.PerfSchemeService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PerfSchemeServiceImpl extends ServiceImpl<PerfSchemeMapper, PerfSchemeEntity> implements PerfSchemeService {

    private final PerfRuleService perfRuleService;
    private final PerfRuleResultMapper perfRuleResultMapper;

    // -------------------- 已有：分页 --------------------
    @Override
    public IPage<PerfSchemeResponse> pageResult(Page page, PerfSchemePageRequest req) {
        LambdaQueryWrapper<PerfSchemeEntity> qw = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(req.getIds())) {
            qw.in(PerfSchemeEntity::getId, req.getIds());
        } else if (ObjectUtil.isAllNotEmpty(req.getStartNo(), req.getEndNo())) {
            qw.between(PerfSchemeEntity::getId, req.getStartNo(), req.getEndNo());
        } else {
            if (StrUtil.isNotBlank(req.getKeyword())) {
                qw.like(PerfSchemeEntity::getSchemeName, req.getKeyword());
            }
            if (req.getStatus() != null) {
                qw.eq(PerfSchemeEntity::getStatus, req.getStatus());
            }
            if (req.getStartDate() != null) {
                qw.ge(PerfSchemeEntity::getPeriodStart, req.getStartDate());
            }
            if (req.getEndDate() != null) {
                qw.le(PerfSchemeEntity::getPeriodEnd, req.getEndDate());
            }
        }

        qw.orderByDesc(PerfSchemeEntity::getUpdateTime)
                .orderByDesc(PerfSchemeEntity::getCreateTime)
                .orderByDesc(PerfSchemeEntity::getId);

        IPage<PerfSchemeEntity> entityPage = this.page((Page<PerfSchemeEntity>) page, qw);
        return entityPage.convert(this::convertToResponse);
    }

    // -------------------- 已有：详情（含规则） --------------------
    @SneakyThrows
    @Override
    public PerfSchemeWithRulesResponse detailWithRules(Long id) {
        PerfSchemeEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("方案不存在");
        }
        PerfSchemeWithRulesResponse resp = new PerfSchemeWithRulesResponse();
        CopyUtil.copyProperties(entity, resp);

        List<PerfRuleEntity> rules = perfRuleService.lambdaQuery()
                .eq(PerfRuleEntity::getSchemeId, id)
                .orderByAsc(PerfRuleEntity::getOrdNo)
                .orderByAsc(PerfRuleEntity::getId)
                .list();

        List<PerfRuleResponse> ruleRes = rules.stream().map(r -> {
            PerfRuleResponse x = new PerfRuleResponse();
            CopyUtil.copyProperties(r, x);
            return x;
        }).collect(Collectors.toList());

        resp.setRules(ruleRes);
        return resp;
    }

    // -------------------- 新增：保存方案+规则（一次提交） --------------------
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean doSaveOrUpdateWithRules(PerfSchemeSaveRequest req) {
        // 1) 校验
        if (StrUtil.isBlank(req.getSchemeName())) {
            throw new BizException("方案名称不能为空");
        }
        LocalDate ps = req.getPeriodStart(), pe = req.getPeriodEnd();
        if (ps != null && pe != null && pe.isBefore(ps)) {
            throw new BizException("计算周期结束日期不能早于开始日期");
        }
        if (req.getStatus() == null) {
            req.setStatus(1);
        }

        // 2) 保存/更新方案
        PerfSchemeEntity entity = new PerfSchemeEntity();
        CopyUtil.copyProperties(req, entity);
        boolean ok = (entity.getId() == null) ? this.save(entity) : this.updateById(entity);
        if (!ok) throw new BizException("保存方案失败");
        Long schemeId = entity.getId();

        // 3) 替换规则（先删后增） —— 必须通过 PerfRuleService
        perfRuleService.lambdaUpdate()
                .eq(PerfRuleEntity::getSchemeId, schemeId)
                .remove(); // 逻辑删除（@TableLogic）

        if (CollUtil.isEmpty(req.getRules())) {
            return true; // 允许空规则
        }

        // 清洗 + 去重（ipTypeCode + ruleEventCode 不重复）
        Set<String> uniq = new HashSet<>();
        List<PerfRuleEntity> toSave = new ArrayList<>();
        int ord = 1;
        for (PerfRuleItemRequest it : req.getRules()) {
            if (it == null) continue;
            if (StrUtil.isBlank(it.getIpTypeCode()) || StrUtil.isBlank(it.getRuleEventCode())) continue;

            String key = it.getIpTypeCode() + "|" + it.getRuleEventCode();
            if (!uniq.add(key)) {
                throw new BizException("同一方案下不允许重复的类型与事件组合: " + key);
            }

            PerfRuleEntity e = new PerfRuleEntity();
            e.setSchemeId(schemeId);
            e.setOrdNo(it.getOrdNo() != null ? it.getOrdNo() : ord++);
            e.setIpTypeCode(it.getIpTypeCode());
            e.setIpTypeName(it.getIpTypeName());
            e.setRuleEventCode(it.getRuleEventCode());
            e.setRuleEventName(it.getRuleEventName());
            e.setScore(it.getScore());
            e.setStatus(ObjectUtil.defaultIfNull(it.getStatus(), 1));
            e.setExtraCondJson(it.getExtraCondJson());
            e.setRemark(it.getRemark());
            toSave.add(e);
        }

        if (CollUtil.isNotEmpty(toSave)) {
            boolean saved = perfRuleService.saveBatch(toSave);
            if (!saved) throw new BizException("保存规则失败");
        }

        return true;
    }

    // -------------------- 新增：启停切换 --------------------
    @SneakyThrows
    @Override
    public Boolean toggleStatus(Long id) {
        PerfSchemeEntity e = this.getById(id);
        if (e == null) throw new BizException("方案不存在");
        Integer next = (ObjectUtil.equal(e.getStatus(), 1)) ? 0 : 1;
        PerfSchemeEntity upd = new PerfSchemeEntity();
        upd.setId(id);
        upd.setStatus(next);
        return this.updateById(upd);
    }

    // -------------------- 新增：批量删除（方案+规则） --------------------
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) return true;
        // 先删规则（逻辑删除）
        perfRuleService.lambdaUpdate()
                .in(PerfRuleEntity::getSchemeId, ids)
                .remove();
        // 再删方案
        return this.removeBatchByIds(ids);
    }

    @Override
    @SneakyThrows
    public PerfSchemeOverviewResponse overview(Long schemeId) {
        PerfSchemeEntity scheme = getById(schemeId);
        if (scheme == null) throw new BizException("方案不存在");

        BigDecimal totalScore = perfRuleResultMapper.sumTotalScore(schemeId);
        List<PerfSchemeCellResponse> cells = perfRuleResultMapper.selectCellsByScheme(schemeId);
        if (CollUtil.isEmpty(cells)) cells = Collections.emptyList();

        PerfSchemeOverviewResponse resp = new PerfSchemeOverviewResponse();
        resp.setSchemeId(scheme.getId());
        resp.setSchemeName(scheme.getSchemeName());
        resp.setPeriodStart(scheme.getPeriodStart());
        resp.setPeriodEnd(scheme.getPeriodEnd());
        resp.setTotalScore(totalScore);
        resp.setCells(cells);
        return resp;
    }

    // -------------------- 辅助 --------------------
    private PerfSchemeResponse convertToResponse(PerfSchemeEntity e) {
        PerfSchemeResponse r = new PerfSchemeResponse();
        CopyUtil.copyProperties(e, r);
        return r;
    }
}
