package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentProposalCompleterEntity;
import com.pig4cloud.pigx.admin.entity.PatentProposalOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.PatentProposalOwnerMapper;
import com.pig4cloud.pigx.admin.service.PatentProposalOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专利提案—申请人
 *
 * @author pigx
 * @date 2025-05-23 11:33:18
 */
@Service
public class PatentProposalOwnerServiceImpl extends ServiceImpl<PatentProposalOwnerMapper, PatentProposalOwnerEntity> implements PatentProposalOwnerService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceOwners(Long id, List<PatentProposalOwnerEntity> entities) {
        this.remove(Wrappers.<PatentProposalOwnerEntity>lambdaQuery()
                .eq(PatentProposalOwnerEntity::getPatentProposalId, id));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setPatentProposalId(id));
            this.saveBatch(entities);
        }
    }
}