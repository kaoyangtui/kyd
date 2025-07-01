package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentFetchCheckpointEntity;

public interface PatentFetchCheckpointService extends IService<PatentFetchCheckpointEntity> {

    PatentFetchCheckpointEntity getByTaskKey(String taskKey);

    void saveOrUpdateOffset(String taskKey, int offset, Long total);

    void deleteByTaskKey(String taskKey);
}