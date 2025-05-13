package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegCompleterEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SoftCopyRegCompleterService extends IService<SoftCopyRegCompleterEntity> {

    @Transactional(rollbackFor = Exception.class)
    Boolean replaceCompleters(Long regId, List<SoftCopyRegCompleterEntity> completers);
}