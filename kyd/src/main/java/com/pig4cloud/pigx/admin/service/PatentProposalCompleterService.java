package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentProposalCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;

import java.util.List;

public interface PatentProposalCompleterService extends IService<PatentProposalCompleterEntity> {

    void replaceCompleters(Long id, List<PatentProposalCompleterEntity> completerEntities);
}