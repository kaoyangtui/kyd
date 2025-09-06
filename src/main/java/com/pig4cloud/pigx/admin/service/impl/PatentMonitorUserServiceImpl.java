package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.PatentStatusEnum;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserPageRequest;
import com.pig4cloud.pigx.admin.dto.patentMonitor.PatentMonitorUserResponse;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;
import com.pig4cloud.pigx.admin.entity.PatentMonitorUserEntity;
import com.pig4cloud.pigx.admin.mapper.PatentMonitorUserMapper;
import com.pig4cloud.pigx.admin.service.PatentMonitorUserService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatentMonitorUserServiceImpl extends ServiceImpl<PatentMonitorUserMapper, PatentMonitorUserEntity>
        implements PatentMonitorUserService {

    private final PatentMonitorServiceImpl patentMonitorService;
    private final PatentInfoServiceImpl patentInfoService;

    @Override
    public IPage<PatentMonitorUserResponse> pageResult(Page page, PatentMonitorUserPageRequest request) {
        LambdaQueryWrapper<PatentMonitorUserEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(PatentMonitorUserEntity::getCreateUserId, SecurityUtils.getUser().getId());
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            qw.and(wrapper -> wrapper
                    .like(PatentMonitorUserEntity::getPid, request.getKeyword())
                    .or().like(PatentMonitorUserEntity::getTitle, request.getKeyword())
                    .or().like(PatentMonitorUserEntity::getAppNumber, request.getKeyword())
            );
        }
        qw.orderByDesc(PatentMonitorUserEntity::getEventTime, PatentMonitorUserEntity::getCreateTime);

        IPage<PatentMonitorUserEntity> entityPage = this.page(page, qw);

        // 1. 先收集所有PID
        List<String> pidList = entityPage.getRecords().stream()
                .map(PatentMonitorUserEntity::getPid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 2. 一次查出所有PID最新一条日志（按时间倒序）
        List<PatentMonitorEntity> logList = patentMonitorService.lambdaQuery()
                .in(PatentMonitorEntity::getPid, pidList)
                .orderByDesc(PatentMonitorEntity::getEventTime)
                .list();
        // 构建PID->日志的映射，只保留最新一条
        Map<String, PatentMonitorEntity> latestLogMap = logList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        PatentMonitorEntity::getPid,
                        e -> e,
                        // 保留第一条（最新）
                        (oldVal, newVal) -> oldVal
                ));

        // 3. 分页批量赋值
        return entityPage.convert(entity -> {
            PatentMonitorUserResponse resp = BeanUtil.copyProperties(entity, PatentMonitorUserResponse.class);
            PatentMonitorEntity latestLog = latestLogMap.get(entity.getPid());
            if (latestLog != null) {
                resp.setEventTime(latestLog.getEventTime());
                resp.setEventContent(latestLog.getEventContent());
                resp.setEventStatus(PatentStatusEnum.getByCode(latestLog.getStatusCode()).getDescription());
            }
            return resp;
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(List<String> pidList) {
        for (String pid : pidList) {
            PatentMonitorUserEntity old = lambdaQuery()
                    .eq(PatentMonitorUserEntity::getPid, pid)
                    .last("limit 1")
                    .one();
            if (old == null) {
                // 不存在，插入新监控
                PatentMonitorUserEntity entity = new PatentMonitorUserEntity();
                entity.setPid(pid);
                PatentInfoEntity patentInfo = patentInfoService.lambdaQuery()
                        .eq(PatentInfoEntity::getPid, pid).one();
                entity.setTitle(patentInfo.getTitle());
                entity.setPatType(patentInfo.getPatType());
                entity.setAppNumber(patentInfo.getAppNumber());
                this.save(entity);
            } else if ("1".equals(old.getDelFlag())) {
                // 逻辑删除过的，直接恢复
                old.setDelFlag("0");
                this.updateById(old);
            }
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<String> pidList) {
        // 只做逻辑删除
        return this.removeBatchByIds(pidList);
    }
}
