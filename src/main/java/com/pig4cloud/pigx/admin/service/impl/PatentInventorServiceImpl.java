package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimCreateRequest;
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





    @Override
    public List<PatentInventorVO> listByPid(String pid) {
        List<PatentInventorEntity> entityList = this.list(
                new LambdaQueryWrapper<PatentInventorEntity>().eq(PatentInventorEntity::getPid, pid)
        );
        return entityList.stream().map(entity -> {
            String code = entity.getCode();
            PatentInventorVO vo = new PatentInventorVO();
            // 如果 VO 有 id 字段，这里也要赋值
            vo.setId(entity.getId());
            vo.setPriority(entity.getPriority());
            vo.setCode(code);
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



}