package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentProposalOwnerEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyOwnerEntity;

import java.util.List;

public interface PatentProposalOwnerService extends IService<PatentProposalOwnerEntity> {

    void replaceOwners(Long id, List<PatentProposalOwnerEntity> ownerEntities);
}