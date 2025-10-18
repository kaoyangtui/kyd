package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.dto.patentPreEval.*;
import com.pig4cloud.pigx.admin.entity.PatentPreEvalEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentPreEvalMapper;
import com.pig4cloud.pigx.admin.service.PatentPreEvalService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 专利申请前评估 ServiceImpl
 * 约定：
 * 1) Service 层不封装 R
 * 2) 分页与详情只读，写操作加事务
 * 3) 字段转换集中在 convert 方法
 */
@Service
@RequiredArgsConstructor
public class PatentPreEvalServiceImpl extends ServiceImpl<PatentPreEvalMapper, PatentPreEvalEntity>
        implements PatentPreEvalService {

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public Page<PatentPreEvalResponse> pageResult(Page page, PatentPreEvalPageRequest req) {
        LambdaQueryWrapper<PatentPreEvalEntity> qw = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(req.getKeyword())) {
            qw.and(w -> w.like(PatentPreEvalEntity::getTitle, req.getKeyword())
                    .or().like(PatentPreEvalEntity::getApplicant, req.getKeyword()));
        }
        if (StrUtil.isNotBlank(req.getApplicant())) {
            qw.like(PatentPreEvalEntity::getApplicant, req.getApplicant());
        }
        if (ObjectUtil.isNotEmpty(req.getStatus())) {
            qw.eq(PatentPreEvalEntity::getStatus, req.getStatus());
        }
        if (StrUtil.isNotBlank(req.getLevel())) {
            qw.eq(PatentPreEvalEntity::getLevel, req.getLevel());
        }

        if (req.getAppDateRangeBegin() != null) {
            qw.ge(PatentPreEvalEntity::getAppDate, req.getAppDateRangeBegin());
        }
        if (req.getAppDateRangeEnd() != null) {
            qw.le(PatentPreEvalEntity::getAppDate, req.getAppDateRangeEnd());
        }

        if (req.getReportDateRangeBegin() != null) {
            qw.ge(PatentPreEvalEntity::getReportDate, req.getReportDateRangeBegin());
        }
        if (req.getReportDateRangeEnd() != null) {
            qw.le(PatentPreEvalEntity::getReportDate, req.getReportDateRangeEnd());
        }

        qw.orderByDesc(PatentPreEvalEntity::getCreateTime);

        Page<PatentPreEvalEntity> entityPage = this.page(page, qw);
        Page<PatentPreEvalResponse> respPage = new Page<>();
        respPage.setCurrent(entityPage.getCurrent());
        respPage.setSize(entityPage.getSize());
        respPage.setTotal(entityPage.getTotal());
        List<PatentPreEvalResponse> records = entityPage.getRecords().stream()
                .map(this::convertToResponse)
                .toList();
        respPage.setRecords(records);
        return respPage;
    }

    @Override
    @SneakyThrows
    public PatentPreEvalResponse detail(PatentPreEvalDetailRequest req) {
        PatentPreEvalEntity entity;
        if (ObjectUtil.isNotEmpty(req.getId())) {
            entity = this.getById(req.getId());
        } else if (StrUtil.isNotBlank(req.getCode())) {
            entity = this.lambdaQuery()
                    .eq(PatentPreEvalEntity::getCode, req.getCode())
                    .one();
        } else {
            throw new BizException("参数有误，id 或 code 必填其一");
        }
        if (entity == null) {
            throw new BizException("记录不存在或已删除");
        }
        return convertToResponse(entity);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(PatentPreEvalCreateRequest req) {
        PatentPreEvalEntity entity = new PatentPreEvalEntity();
        CopyUtil.copyProperties(req, entity);
        if (StrUtil.isBlank(entity.getCode())) {
            entity.setCode("PPE" + IdUtil.getSnowflakeNextIdStr());
        }
        if (StrUtil.isBlank(entity.getApplyDateRangeType())) {
            entity.setApplyDateRangeType("ANY");
        }
        entity.setStatus(0);
        validEnumsOnSaveOrUpdate(entity);
        this.save(entity);
        PatentEvaluationMqRequest mqReq = new PatentEvaluationMqRequest();
        mqReq.setSysFlag(TopicConstants.TOPIC_PATENT_TAG);
        mqReq.setSysBizId(String.valueOf(entity.getId()));
        mqReq.setTitle(entity.getTitle());
        mqReq.setAppDate(entity.getAppDate());
        mqReq.setApplicant(entity.getApplicant());
        mqReq.setApplyDateRangeType(entity.getApplyDateRangeType());
        mqReq.setAbstractText(entity.getAbstractText());
        mqReq.setClaimText(entity.getClaimText());
        mqReq.setDescriptionText(entity.getDescriptionText());
        // 全局顺序
        rocketMQTemplate.syncSendOrderly(TopicConstants.TOPIC_PATENT_EVALUATION, JSONUtil.toJsonStr(mqReq), "global");
        return true;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(PatentPreEvalUpdateRequest req) {
        if (req.getId() == null) throw new BizException("id 不能为空");
        PatentPreEvalEntity db = this.getById(req.getId());
        if (db == null) throw new BizException("记录不存在或已删除");

        BeanUtil.copyProperties(
                req, db,
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setIgnoreProperties("id", "status", "level", "reportDate",
                                "viability", "tech", "market", "reportUrl",
                                "createUser", "createTime", "updateUser", "updateTime")
        );
        validEnumsOnSaveOrUpdate(db);
        return this.updateById(db);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateResult(PatentPreEvalUpdateResultRequest req) {
        if (req.getId() == null) {
            throw new BizException("id 不能为空");
        }
        PatentPreEvalEntity db = this.getById(req.getId());
        if (db == null) {
            throw new BizException("记录不存在或已删除");
        }
        if (ObjectUtil.isNotEmpty(req.getStatus())) {
            db.setStatus(req.getStatus());
        }
        if (StrUtil.isNotBlank(req.getLevel())) {
            db.setLevel(req.getLevel());
        }
        if (ObjectUtil.isNotEmpty(req.getReportDate())) {
            db.setReportDate(req.getReportDate());
        }
        if (StrUtil.isNotBlank(req.getViability())) {
            db.setViability(req.getViability());
        }
        if (StrUtil.isNotBlank(req.getTech())) {
            db.setTech(req.getTech());
        }
        if (StrUtil.isNotBlank(req.getMarket())) {
            db.setMarket(req.getMarket());
        }
        if (StrUtil.isNotBlank(req.getReportUrl())) {
            db.setReportUrl(req.getReportUrl());
        }
        validResultFields(db);
        return this.updateById(db);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ids 不能为空");
        }
        return this.removeByIds(ids);
    }

    private PatentPreEvalResponse convertToResponse(PatentPreEvalEntity e) {
        PatentPreEvalResponse r = new PatentPreEvalResponse();
        CopyUtil.copyProperties(e, r);
        r.setAppDate(e.getAppDate());
        r.setReportDate(e.getReportDate());
        r.setCreateBy(e.getCreateUser());
        r.setUpdateBy(e.getUpdateUser());
        r.setCreateTime(e.getCreateTime());
        r.setUpdateTime(e.getUpdateTime());
        return r;
    }

    private void validEnumsOnSaveOrUpdate(PatentPreEvalEntity e) throws BizException {
        if (StrUtil.isNotBlank(e.getApplyDateRangeType())) {
            boolean ok = Objects.equals(e.getApplyDateRangeType(), "ANY")
                    || Objects.equals(e.getApplyDateRangeType(), "BEFORE_EXCLUSIVE")
                    || Objects.equals(e.getApplyDateRangeType(), "BEFORE_INCLUSIVE");
            if (!ok) {
                throw new BizException("申请日范围类型不合法");
            }
        }
        if (StrUtil.isNotBlank(e.getLevel())) {
            boolean ok = StrUtil.equalsAny(e.getLevel(), "A", "B", "C", "D");
            if (!ok) {
                throw new BizException("评估等级仅支持 A/B/C/D");
            }
        }
        validResultFields(e);
    }

    private void validResultFields(PatentPreEvalEntity e) throws BizException {
        if (e.getStatus() != null && e.getStatus() != 0 && e.getStatus() != 1 && e.getStatus() != 2) {
            throw new BizException("结果状态仅支持 0/1/2");
        }
        if (StrUtil.isNotBlank(e.getViability())) {
            validStar(e.getViability(), "可专利性");
        }
        if (StrUtil.isNotBlank(e.getTech())) {
            validStar(e.getTech(), "技术竞争");
        }
        if (StrUtil.isNotBlank(e.getMarket())) {
            validStar(e.getMarket(), "市场前景");
        }
    }

    private void validStar(String v, String label) throws BizException {
        boolean ok = StrUtil.equalsAny(v, "1", "2", "3", "4", "5");
        if (!ok) {
            throw new BizException(label + "星级必须为 1-5");
        }
    }
}
