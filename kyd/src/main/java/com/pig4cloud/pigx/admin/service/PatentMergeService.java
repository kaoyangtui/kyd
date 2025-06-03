package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentMergeEntity;

public interface PatentMergeService extends IService<PatentMergeEntity> {

    void create(String message);
}