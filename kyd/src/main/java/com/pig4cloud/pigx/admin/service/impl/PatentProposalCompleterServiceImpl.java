package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentProposalCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;
import com.pig4cloud.pigx.admin.mapper.PatentProposalCompleterMapper;
import com.pig4cloud.pigx.admin.service.PatentProposalCompleterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专利提案—发明人
 *
 * @author pigx
 * @date 2025-05-23 11:34:04
 */
@Service
public class PatentProposalCompleterServiceImpl extends ServiceImpl<PatentProposalCompleterMapper, PatentProposalCompleterEntity> implements PatentProposalCompleterService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceCompleters(Long id, List<PatentProposalCompleterEntity> entities) {
        this.remove(Wrappers.<PatentProposalCompleterEntity>lambdaQuery()
                .eq(PatentProposalCompleterEntity::getPatentProposalId, id));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> e.setPatentProposalId(id));
            this.saveBatch(entities);
        }
    }
}