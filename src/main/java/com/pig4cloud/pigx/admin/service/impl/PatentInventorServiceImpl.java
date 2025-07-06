package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.entity.PatentInventorEntity;
import com.pig4cloud.pigx.admin.mapper.PatentInventorMapper;
import com.pig4cloud.pigx.admin.service.PatentInventorService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

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
}