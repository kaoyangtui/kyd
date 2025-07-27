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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatentMonitorUserServiceImpl extends ServiceImpl<PatentMonitorUserMapper, PatentMonitorUserEntity>
        implements PatentMonitorUserService {

    private final PatentMonitorServiceImpl patentMonitorService;
    private final PatentInfoServiceImpl patentInfoService;

    @Override
    public IPage<PatentMonitorUserResponse> pageResult(Page page, PatentMonitorUserPageRequest request) {
        LambdaQueryWrapper<PatentMonitorUserEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(PatentMonitorUserEntity::getDelFlag, "0");
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            qw.and(wrapper -> wrapper
                    .like(PatentMonitorUserEntity::getPid, request.getKeyword())
                    .or().like(PatentMonitorUserEntity::getTitle, request.getKeyword())
                    .or().like(PatentMonitorUserEntity::getAppNumber, request.getKeyword())
            );
        }
        qw.orderByDesc(PatentMonitorUserEntity::getEventTime, PatentMonitorUserEntity::getCreateTime);

        IPage<PatentMonitorUserEntity> entityPage = this.page(page, qw);

        // lambdaQuery补全每个专利的最新监控日志（N+1方式，纯Lambda实现）
        return entityPage.convert(entity -> {
            PatentMonitorUserResponse resp = BeanUtil.copyProperties(entity, PatentMonitorUserResponse.class);

            // 查找 t_patent_monitor 的最新一条日志
            PatentMonitorEntity latestLog = patentMonitorService.lambdaQuery()
                    .eq(PatentMonitorEntity::getPid, entity.getPid())
                    .orderByDesc(PatentMonitorEntity::getEventTime)
                    .last("limit 1")
                    .one();

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
        LambdaQueryWrapper<PatentMonitorUserEntity> qw = new LambdaQueryWrapper<>();
        qw.in(PatentMonitorUserEntity::getPid, pidList);
        return lambdaUpdate().set(PatentMonitorUserEntity::getDelFlag, "1").apply(qw.getSqlSegment()).update();
    }
}
