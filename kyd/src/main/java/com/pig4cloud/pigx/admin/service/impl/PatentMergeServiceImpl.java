package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.PatentMergeEntity;
import com.pig4cloud.pigx.admin.mapper.PatentMergeMapper;
import com.pig4cloud.pigx.admin.service.PatentMergeService;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import org.springframework.stereotype.Service;

/**
 * 专利合并表
 *
 * @author pigx
 * @date 2025-05-31 11:03:08
 */
@Service
public class PatentMergeServiceImpl extends ServiceImpl<PatentMergeMapper, PatentMergeEntity> implements PatentMergeService {
    @Override
    public void create(String message) {
        PatentMergeEntity patentMerge = JSONUtil.toBean(message, PatentMergeEntity.class);
        patentMerge.setId(null);
        patentMerge.setAppNumber(CodeUtils.getFirstCode(patentMerge.getAppNumber()));
        patentMerge.setPubNumber(CodeUtils.getFirstCode(patentMerge.getPubNumber()));
        patentMerge.setApplicantName(CodeUtils.formatCodes(patentMerge.getApplicantName()));
        patentMerge.setApplicantType(CodeUtils.formatCodes(patentMerge.getApplicantType()));
        patentMerge.setInventorName(CodeUtils.formatCodes(patentMerge.getInventorName()));
        patentMerge.setPatentee(CodeUtils.formatCodes(patentMerge.getPatentee()));
        patentMerge.setNec(CodeUtils.formatCodes(patentMerge.getNec()));
        patentMerge.setIpc(CodeUtils.formatCodes(patentMerge.getIpc()));
        patentMerge.setIpcSection(CodeUtils.formatCodes(patentMerge.getIpcSection()));
        patentMerge.setIpcClass(CodeUtils.formatCodes(patentMerge.getIpcClass()));
        patentMerge.setIpcSubClass(CodeUtils.formatCodes(patentMerge.getIpcSubClass()));
        patentMerge.setIpcGroup(CodeUtils.formatCodes(patentMerge.getIpcGroup()));
        patentMerge.setIpcSubGroup(CodeUtils.formatCodes(patentMerge.getIpcSubGroup()));
        patentMerge.setAgentName(CodeUtils.formatCodes(patentMerge.getAgentName()));
        patentMerge.setPatentWords(CodeUtils.formatCodes(patentMerge.getPatentWords()));
        patentMerge.setTitleKey(CodeUtils.formatCodes(patentMerge.getTitleKey()));
        patentMerge.setClKey(CodeUtils.formatCodes(patentMerge.getClKey()));
        patentMerge.setBgKey(CodeUtils.formatCodes(patentMerge.getBgKey()));
        patentMerge.setHistoryPatentee(CodeUtils.formatCodes(patentMerge.getHistoryPatentee()));
        patentMerge.setPatenteType(CodeUtils.formatCodes(patentMerge.getPatenteType()));
        PatentMergeEntity oldPatentMerge = this.lambdaQuery()
                .eq(PatentMergeEntity::getAppNumber, patentMerge.getAppNumber())
                .one();
        if (oldPatentMerge != null) {
            this.removeById(oldPatentMerge);
        }
        this.save(patentMerge);
    }
}