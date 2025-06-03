package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentInventorEntity;
import com.pig4cloud.pigx.admin.mapper.PatentInventorMapper;
import com.pig4cloud.pigx.admin.service.PatentInventorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专利发明人
 *
 * @author pigx
 * @date 2025-05-31 10:46:36
 */
@Service
public class PatentInventorServiceImpl extends ServiceImpl<PatentInventorMapper, PatentInventorEntity> implements PatentInventorService {
    @Override
    public void create(String pid, List<String> inventorNames) {
        Long cnt = this.lambdaQuery().eq(PatentInventorEntity::getPid, pid).count();
        if (cnt > 0) {
            return;
        }
        // 遍历发明人名单，并按顺序保存到数据库
        for (int i = 0; i < inventorNames.size(); i++) {
            PatentInventorEntity inventor = new PatentInventorEntity();
            // 设置专利PID
            inventor.setPid(pid);
            // 设置发明人姓名
            inventor.setName(inventorNames.get(i));
            // 设置发明人顺位，注意顺位从1开始
            inventor.setPriority(String.valueOf(i + 1));
            // 保存发明人实体
            this.save(inventor);
        }
    }
}