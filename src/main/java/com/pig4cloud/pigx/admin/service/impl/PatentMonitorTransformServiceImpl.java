package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.PatentStatusEnum;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorTransformResponse;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorTransformEntity;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorTransformMapper;
import com.pig4cloud.pigx.admin.service.PatentInfoService;
import com.pig4cloud.pigx.admin.service.PatentMonitorService;
import com.pig4cloud.pigx.admin.service.PatentMonitorTransformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 专利转化监控表
 *
 * @author pigx
 * @date 2025-07-28 16:09:48
 */
@RequiredArgsConstructor
@Service
public class PatentMonitorTransformServiceImpl extends ServiceImpl<PatentMonitorTransformMapper, PatentMonitorTransformEntity> implements PatentMonitorTransformService {

    private final PatentInfoService patentInfoService;
    private final PatentMonitorService patentMonitorService;

    @Override
    public IPage<PatentMonitorTransformResponse> pageResult(Page page, PatentMonitorTransformPageRequest request) {
        LambdaQueryWrapper<PatentMonitorTransformEntity> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getTransformKeyword())) {
            qw.and(w ->
                    w.like(PatentMonitorTransformEntity::getName, request.getTransformKeyword())
                    .or().like(PatentMonitorTransformEntity::getCode, request.getTransformKeyword()));
        }
        if (StrUtil.isNotBlank(request.getPatentKeyword())) {
            qw.and(w ->
                    w.like(PatentMonitorTransformEntity::getTitle, request.getPatentKeyword())
                    .or().like(PatentMonitorTransformEntity::getAppNumber, request.getPatentKeyword()));
        }
        qw.orderByDesc(PatentMonitorTransformEntity::getUpdateTime, PatentMonitorTransformEntity::getCreateTime);

        IPage<PatentMonitorTransformEntity> entityPage = this.page(page, qw);
        // 1. 提前收集所有pid
        List<String> pidList = entityPage.getRecords().stream()
                .map(PatentMonitorTransformEntity::getPid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (pidList.isEmpty()) {
            return null;
        }
        // 2. 一次查出所有pid的最新日志
        List<PatentMonitorEntity> logList = patentMonitorService.lambdaQuery()
                .in(PatentMonitorEntity::getPid, pidList)
                .orderByDesc(PatentMonitorEntity::getEventTime)
                .list();
        Map<String, PatentMonitorEntity> latestLogMap = logList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        PatentMonitorEntity::getPid,
                        e -> e,
                        (oldVal, newVal) -> oldVal // 已排序，保留第一次即最新
                ));

        // 3. 一次查出所有pid的专利信息
        List<PatentInfoEntity> infoList = patentInfoService.lambdaQuery()
                .in(PatentInfoEntity::getPid, pidList)
                .list();
        Map<String, PatentInfoEntity> infoMap = infoList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(PatentInfoEntity::getPid, e -> e, (a, b) -> a));

        // 4. 批量转换赋值
        return entityPage.convert(entity -> {
            PatentMonitorTransformResponse resp = BeanUtil.copyProperties(entity, PatentMonitorTransformResponse.class);

            // 日志表赋值
            PatentMonitorEntity latestLog = latestLogMap.get(entity.getPid());
            if (latestLog != null) {
                resp.setEventTime(latestLog.getEventTime());
                resp.setEventContent(latestLog.getEventContent());
                resp.setEventStatus(PatentStatusEnum.getByCode(latestLog.getStatusCode()).getDescription());
            }

            // 专利表赋值
            PatentInfoEntity patentInfo = infoMap.get(entity.getPid());
            if (patentInfo != null) {
                resp.setLprs(patentInfo.getLprs());
                resp.setPatentee(patentInfo.getPatentee());
            }
            return resp;
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(String pid,
                          String code,
                          String name,
                          LocalDate signDate,
                          LocalDate expireDate,
                          Long createUserId) {

        PatentMonitorTransformEntity old = lambdaQuery()
                .eq(PatentMonitorTransformEntity::getPid, pid)
                .last("limit 1")
                .one();
        if (old == null) {
            PatentInfoEntity patentInfo = patentInfoService.lambdaQuery()
                    .eq(PatentInfoEntity::getPid, pid).one();
            if (patentInfo == null) {
                return false;
            }
            PatentMonitorTransformEntity entity = new PatentMonitorTransformEntity();
            entity.setPid(pid);
            entity.setAppNumber(patentInfo.getAppNumber());
            entity.setTitle(patentInfo.getTitle());
            entity.setCode(code);
            entity.setName(name);
            entity.setPatType(patentInfo.getPatType());
            entity.setSignDate(signDate);
            entity.setExpireDate(expireDate);
            entity.setCreateUserId(createUserId);
            this.save(entity);
        } else if ("1".equals(old.getDelFlag())) {
            old.setDelFlag("0");
            this.updateById(old);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> idList) {
        return lambdaUpdate()
                .in(PatentMonitorTransformEntity::getId, idList)
                .remove();
    }
}