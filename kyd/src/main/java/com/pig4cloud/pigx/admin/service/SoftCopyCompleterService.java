package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SoftCopyCompleterService extends IService<SoftCopyCompleterEntity> {

    void replaceCompleters(Long softCopyId, List<SoftCopyCompleterEntity> entities);
}