package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentInventorVO;
import com.pig4cloud.pigx.admin.dto.patent.PatentUnClaimRequest;
import com.pig4cloud.pigx.admin.entity.ExpertEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.entity.PatentInventorEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentInventorMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 专利发明人
 *
 * @author pigx
 * @date 2025-05-31 10:46:36
 */
@RequiredArgsConstructor
@Service
public class PatentInventorServiceImpl extends ServiceImpl<PatentInventorMapper, PatentInventorEntity> implements PatentInventorService {

    private final SysUserService sysUserService;
    private final ExpertService expertService;
    private final PatentInfoService patentInfoService;
    private final PatentProposalService patentProposalService;

    @Override
    public void create(String pid, List<String> inventorNames) {
        List<SysUser> sysUserList = sysUserService.lambdaQuery()
                .in(SysUser::getName, inventorNames)
                .list();
        // 遍历发明人名单，并按顺序保存到数据库
        List<PatentInventorEntity> patentInventorList = Lists.newArrayList();
        for (int i = 0; i < inventorNames.size(); i++) {
            String inventorName = inventorNames.get(i);
            Long cnt = this.lambdaQuery()
                    .eq(PatentInventorEntity::getPid, pid)
                    .eq(PatentInventorEntity::getPriority, i + 1)
                    .eq(PatentInventorEntity::getName, inventorName)
                    .count();
            if (cnt > 0) {
                //已经处理过发明人信息
                continue;
            }
            boolean isMatch = false;
            for (SysUser sysUser : sysUserList) {
                if (inventorName.equals(sysUser.getName())) {
                    isMatch = true;
                    PatentInventorEntity inventor = new PatentInventorEntity();
                    // 设置专利PID
                    inventor.setPid(pid);
                    // 设置发明人顺位，注意顺位从1开始
                    inventor.setPriority(String.valueOf(i + 1));
                    inventor.setCode(sysUser.getCode());
                    // 设置发明人姓名
                    inventor.setName(inventorNames.get(i));
                    inventor.setDeptId(sysUser.getDeptId());
                    inventor.setDeptName(sysUser.getDeptName());
                    inventor.setContactNumber(sysUser.getPhone());
                    inventor.setEmail(sysUser.getEmail());
                    inventor.setIsLeader(0);
                    patentInventorList.add(inventor);

                    boolean bl = expertService.lambdaQuery()
                            .eq(ExpertEntity::getCode, sysUser.getCode())
                            .exists();
                    if (bl) {
                        expertService.lambdaUpdate()
                                .eq(ExpertEntity::getCode, sysUser.getCode())
                                .setSql("patent_cnt = ifnull(patent_cnt,0) + 1")
                                .update();
                    } else {
                        ExpertEntity expert = new ExpertEntity();
                        expert.setTenantId(1L);
                        expert.setCode(sysUser.getCode());
                        expert.setName(sysUser.getName());
                        expert.setOrgName(sysUser.getDeptName());
                        expert.setShelfStatus(0);
                        expert.setPatentCnt(1L);
                        expert.setResultCnt(0L);
                        expertService.save(expert);
                    }
                }
            }
            if (!isMatch) {
                PatentInventorEntity inventor = new PatentInventorEntity();
                // 设置专利PID
                inventor.setPid(pid);
                // 设置发明人顺位，注意顺位从1开始
                inventor.setPriority(String.valueOf(i + 1));
                // 设置发明人姓名
                inventor.setName(inventorNames.get(i));
                inventor.setIsLeader(0);
                inventor.setRemark("没有匹配到校内用户");
                patentInventorList.add(inventor);
            }
        }
        this.saveBatch(patentInventorList);
    }


    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean claim(PatentClaimRequest req) {
        if (req == null || StrUtil.isBlank(req.getPid()) || CollUtil.isEmpty(req.getPatentInventor())) {
            throw new BizException("认领信息不能为空");
        }
        String pid = req.getPid();
        List<PatentInventorVO> inventorVOList = req.getPatentInventor();

        // 1. 查所有当前发明人
        List<PatentInventorEntity> dbList = this.list(
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
        this.updateBatchById(
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
        patentInfoService.lambdaUpdate().eq(PatentInfoEntity::getPid, pid)
                .set(PatentInfoEntity::getClaimFlag, 1)
                .update();
        if (req.getPatentProposalId() != null) {
            patentProposalService.lambdaUpdate()
                    .eq(PatentProposalEntity::getId, req.getPatentProposalId())
                    .set(PatentProposalEntity::getPid, pid)
                    .update();
        }
        return true;
    }


    @Override
    public List<PatentInventorVO> listByPid(String pid) {
        List<PatentInventorEntity> entityList = this.list(
                new LambdaQueryWrapper<PatentInventorEntity>().eq(PatentInventorEntity::getPid, pid)
        );
        return entityList.stream().map(entity -> {
            PatentInventorVO vo = new PatentInventorVO();
            // 如果 VO 有 id 字段，这里也要赋值
            vo.setId(entity.getId());
            vo.setPriority(entity.getPriority());
            vo.setCode(entity.getCode());
            vo.setName(entity.getName());
            vo.setDeptId(entity.getDeptId());
            vo.setDeptName(entity.getDeptName());
            vo.setContactNumber(entity.getContactNumber());
            vo.setEmail(entity.getEmail());
            vo.setIsLeader(entity.getIsLeader());
            vo.setRemark(entity.getRemark());
            return vo;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public Boolean unClaim(PatentUnClaimRequest req) {
        if (req == null || StrUtil.isBlank(req.getPid())) {
            throw new BizException("PID不能为空");
        }
        Long userId = SecurityUtils.getUser().getId();
        SysUser sysUser = sysUserService.getById(userId);
        this.lambdaUpdate()
                .eq(PatentInventorEntity::getPid, req.getPid())
                .eq(PatentInventorEntity::getCode, sysUser.getCode())
                .remove();
        return null;
    }

}