package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentFetchCheckpointEntity;
import com.pig4cloud.pigx.admin.mapper.PatentFetchCheckpointMapper;
import com.pig4cloud.pigx.admin.service.PatentFetchCheckpointService;
import org.springframework.stereotype.Service;

/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-18 20:08:15
 */
@Service
public class PatentFetchCheckpointServiceImpl extends ServiceImpl<PatentFetchCheckpointMapper, PatentFetchCheckpointEntity> implements PatentFetchCheckpointService {

    @Override
    public PatentFetchCheckpointEntity getByTaskKey(String taskKey) {
        return baseMapper.selectOne(new QueryWrapper<PatentFetchCheckpointEntity>()
                .eq("task_key", taskKey));
    }

    @Override
    public void saveOrUpdateOffset(String taskKey, int offset, Long total) {
        PatentFetchCheckpointEntity chk = getByTaskKey(taskKey);
        if (chk == null) {
            chk = new PatentFetchCheckpointEntity();
            chk.setTaskKey(taskKey);
            chk.setOffset(offset);
            chk.setTotal(total);
            baseMapper.insert(chk);
        } else {
            chk.setOffset(offset);
            chk.setTotal(total);
            baseMapper.updateById(chk);
        }
    }

    @Override
    public void deleteByTaskKey(String taskKey) {
        baseMapper.delete(new QueryWrapper<PatentFetchCheckpointEntity>()
                .eq("task_key", taskKey));
    }
}