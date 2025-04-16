package com.pig4cloud.pigx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.entity.PatentLogEntity;

/**
 * @author zhaoliang
 */
public interface PatentLogService extends IService<PatentLogEntity> {

    Boolean updateStatus();
}