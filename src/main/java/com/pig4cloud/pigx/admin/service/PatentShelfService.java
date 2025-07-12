package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patent.PatentShelfRequest;
import com.pig4cloud.pigx.admin.entity.PatentShelfEntity;

public interface PatentShelfService extends IService<PatentShelfEntity> {

    Boolean shelf(PatentShelfRequest request);
}