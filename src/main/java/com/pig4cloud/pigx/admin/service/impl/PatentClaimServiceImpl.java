package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.constants.FlowStatusEnum;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.entity.PatentClaimEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentInventorEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdateDTO;
import com.pig4cloud.pigx.admin.jsonflow.FlowStatusUpdater;
import com.pig4cloud.pigx.admin.jsonflow.JsonFlowHandle;
import com.pig4cloud.pigx.admin.mapper.PatentClaimMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatentClaimServiceImpl extends ServiceImpl<PatentClaimMapper, PatentClaimEntity> implements PatentClaimService, FlowStatusUpdater {

    private final SysUserService sysUserService;
    private final PatentInfoService patentInfoService;
    private final PatentInventorService patentInventorService;
    private final PatentProposalService patentProposalService;
    private final JsonFlowHandle jsonFlowHandle;

    @Override
    public IPage<PatentClaimResponse> pageResult(Page reqPage, PatentClaimPageRequest request) {
        LambdaQueryWrapper<PatentClaimEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PatentClaimEntity::getId, request.getIds());
        } else {
            wrapper.eq(StrUtil.isNotBlank(request.getCode()), PatentClaimEntity::getCode, request.getCode());
            wrapper.like(StrUtil.isNotBlank(request.getTitle()), PatentClaimEntity::getTitle, request.getTitle());
            wrapper.like(StrUtil.isNotBlank(request.getAppNumber()), PatentClaimEntity::getAppNumber, request.getAppNumber());
            wrapper.like(StrUtil.isNotBlank(request.getInventorName()), PatentClaimEntity::getInventorName, request.getInventorName());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PatentClaimEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getFlowInstId()), PatentClaimEntity::getFlowInstId, request.getFlowInstId());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PatentClaimEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), PatentClaimEntity::getDeptId, request.getDeptId());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptName()), PatentClaimEntity::getDeptName, request.getDeptName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PatentClaimEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PatentClaimEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        if (CollUtil.isEmpty(reqPage.orders())) {
            wrapper.orderByDesc(PatentClaimEntity::getCreateTime);
        }

        IPage<PatentClaimEntity> page = this.page(reqPage, wrapper);

        return page.convert(entity -> BeanUtil.copyProperties(entity, PatentClaimResponse.class));
    }

    @Override
    @SneakyThrows
    public PatentClaimResponse getDetail(Long id) {
        PatentClaimEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return BeanUtil.copyProperties(entity, PatentClaimResponse.class);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeClaims(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean claim(PatentClaimCreateRequest req, boolean isClaim) {
        if (req == null || StrUtil.isBlank(req.getPid()) || CollUtil.isEmpty(req.getPatentInventor())) {
            throw new BizException("认领信息不能为空");
        }
        String pid = req.getPid();
        List<PatentInventorVO> inventorVOList = req.getPatentInventor();

        // 1. 查所有当前发明人
        List<PatentInventorEntity> dbList = patentInventorService.list(
                new LambdaQueryWrapper<PatentInventorEntity>().eq(PatentInventorEntity::getPid, pid)
        );

        // 2. 认领列表中的所有 id
        Set<Long> claimedIds = inventorVOList.stream()
                .map(PatentInventorVO::getId)
                .collect(Collectors.toSet());

        // 3. 保证负责人只有一个
        long leaderCount = inventorVOList.stream().filter(vo -> ObjectUtil.equal(vo.getIsLeader(), 1)).count();
        if (leaderCount == 0 && inventorVOList.size() > 0) {
            inventorVOList.get(0).setIsLeader(1);
            for (int i = 1; i < inventorVOList.size(); i++) {
                inventorVOList.get(i).setIsLeader(0);
            }
        } else {
            boolean found = false;
            for (PatentInventorVO vo : inventorVOList) {
                if (ObjectUtil.equal(vo.getIsLeader(), 1)) {
                    if (!found) {
                        found = true;
                    } else {
                        vo.setIsLeader(0);
                    }
                } else {
                    vo.setIsLeader(0);
                }
            }
        }

        // 4. 先更新已认领的
        Map<Long, PatentInventorEntity> dbMap = dbList.stream()
                .collect(Collectors.toMap(PatentInventorEntity::getId, e -> e));
        for (PatentInventorVO vo : inventorVOList) {
            PatentInventorEntity entity = dbMap.get(vo.getId());
            if (entity == null) {
                throw new BizException("找不到指定的发明人 id: " + vo.getId());
            }
            entity.setCode(vo.getCode());
            entity.setDeptId(vo.getDeptId());
            entity.setDeptName(vo.getDeptName());
            entity.setContactNumber(vo.getContactNumber());
            entity.setEmail(vo.getEmail());
            entity.setIsLeader(vo.getIsLeader());
            entity.setRemark(vo.getRemark());
        }

        // 5. 找到未认领但 priority 一样、name 一样的（重名未认领），做逻辑删除
        // 如果有 deleted 字段，建议 update deleted=1；否则可直接删除
        Set<String> claimedPriorityName = inventorVOList.stream()
                .map(vo -> vo.getPriority() + "|" + vo.getName())
                .collect(Collectors.toSet());
        List<PatentInventorEntity> toDelete = dbList.stream()
                .filter(db -> !claimedIds.contains(db.getId()) &&
                        claimedPriorityName.contains(db.getPriority() + "|" + db.getName()))
                .collect(Collectors.toList());

        // 6. 批量更新
        patentInventorService.updateBatchById(
                inventorVOList.stream().map(vo -> {
                    PatentInventorEntity entity = dbMap.get(vo.getId());
                    return entity;
                }).collect(Collectors.toList())
        );

        // 7. 逻辑删除重名未认领
        if (CollUtil.isNotEmpty(toDelete)) {
            // 有 deleted 字段建议这样
            // toDelete.forEach(e -> e.setDeleted(1));
            // this.updateBatchById(toDelete);

            // 没 deleted 字段直接删除
            List<Long> delIds = toDelete.stream().map(PatentInventorEntity::getId).collect(Collectors.toList());
            this.removeByIds(delIds);
        }
        // 8. 更新专利信息
        patentInfoService.lambdaUpdate().eq(PatentInfoEntity::getPid, pid)
                .set(PatentInfoEntity::getClaimFlag, 1)
                .update();
        if (req.getPatentProposalId() != null) {
            patentProposalService.lambdaUpdate()
                    .eq(PatentProposalEntity::getId, req.getPatentProposalId())
                    .set(PatentProposalEntity::getPid, pid)
                    .update();
        }
        // 9. 创建认领工单
        PatentInfoEntity patentInfo = patentInfoService.lambdaQuery().eq(PatentInfoEntity::getPid, pid).one();
        PatentClaimEntity entity = new PatentClaimEntity();
        String code = ParamResolver.getStr(PatentClaimResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr();
        entity.setPid(patentInfo.getPid());
        entity.setCode(code);
        entity.setAppNumber(patentInfo.getAppNumber());
        entity.setTitle(patentInfo.getTitle());
        entity.setInventorName(patentInfo.getInventorName());
        entity.setFlowKey(PatentClaimResponse.BIZ_CODE);
        entity.setFlowInstId(IdUtil.getSnowflakeNextIdStr());
        this.save(entity);
        // 10. 发起流程
        jsonFlowHandle.startFlow(BeanUtil.beanToMap(entity), entity.getTitle());
        return true;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean unClaim(PatentUnClaimRequest req) {
        if (req == null || StrUtil.isBlank(req.getPid())) {
            throw new BizException("PID不能为空");
        }

        // 1) 当前登录用户
        Long userId = SecurityUtils.getUser().getId();
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null || StrUtil.isBlank(sysUser.getCode())) {
            throw new BizException("无法获取当前登录用户信息或工号为空");
        }

        // 2) 查找“我”认领的那条记录（同一个 pid + 我的工号）
        PatentInventorEntity myRecord = patentInventorService.getOne(
                Wrappers.<PatentInventorEntity>lambdaQuery()
                        .eq(PatentInventorEntity::getPid, req.getPid())
                        .eq(PatentInventorEntity::getCode, sysUser.getCode())
                        .last("limit 1")
        );

        if (myRecord == null) {
            // 业务可选：不抛错，返回 false；也可抛 BizException 看你偏好
            throw new BizException("未找到您的认领记录，无法取消");
            // return false;
        }

        // 3) 查找该 pid + priority 下所有记录（确保至少保留一条）
        String priority = myRecord.getPriority();
        var samePriorityList = patentInventorService.list(
                Wrappers.<PatentInventorEntity>lambdaQuery()
                        .eq(PatentInventorEntity::getPid, req.getPid())
                        .eq(PatentInventorEntity::getPriority, priority)
        );

        if (samePriorityList == null || samePriorityList.isEmpty()) {
            // 理论上不可能：至少应该有 myRecord
            throw new BizException("数据异常：未找到同顺位记录");
        }

        if (samePriorityList.size() > 1) {
            // 4) 同顺位有多条 → 可以直接删除“我”的这条
            boolean removed = patentInventorService.removeById(myRecord.getId());
            if (!removed) {
                throw new BizException("取消认领失败：删除记录异常");
            }
            return true;
        } else {
            // 5) 同顺位仅一条 → 只能清空“我”的个人信息字段，保留这条
            boolean updated = patentInventorService.lambdaUpdate()
                    .eq(PatentInventorEntity::getId, myRecord.getId())
                    // 这里显式 set 为 null/0，更直观且不受 @TableField(updateStrategy) 影响
                    .set(PatentInventorEntity::getCode, null)
                    .set(PatentInventorEntity::getDeptId, null)
                    .set(PatentInventorEntity::getDeptName, null)
                    .set(PatentInventorEntity::getContactNumber, null)
                    .set(PatentInventorEntity::getEmail, null)
                    .set(PatentInventorEntity::getIsLeader, 0)
                    .set(PatentInventorEntity::getRemark, "取消认领：清空个人信息并保留记录")
                    .update();
            if (!updated) {
                throw new BizException("取消认领失败：更新记录异常");
            }
            return true;
        }
    }


    @Override
    public String flowKey() {
        return PatentClaimResponse.BIZ_CODE;
    }

    @Override
    public void update(FlowStatusUpdateDTO dto) {
        this.lambdaUpdate()
                .eq(PatentClaimEntity::getFlowInstId, dto.getFlowInstId())
                .set(dto.getFlowStatus() != null, PatentClaimEntity::getFlowStatus, dto.getFlowStatus())
                .set(StrUtil.isNotBlank(dto.getCurrentNodeName()), PatentClaimEntity::getCurrentNodeName, dto.getCurrentNodeName())
                .update();
        if (dto.getFlowStatus() != null && dto.getFlowStatus().equals(FlowStatusEnum.INVALID.getStatus())) {
            String pid = this.lambdaQuery().eq(PatentClaimEntity::getFlowInstId, dto.getFlowInstId()).one().getPid();
            patentInfoService.lambdaUpdate().eq(PatentInfoEntity::getPid, pid)
                    .set(PatentInfoEntity::getClaimFlag, 0)
                    .update();
        }
    }
}
