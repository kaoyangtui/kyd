package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentMonitorEntity;

public interface PatentMonitorService extends IService<PatentMonitorEntity> {

    void create(String message);
}