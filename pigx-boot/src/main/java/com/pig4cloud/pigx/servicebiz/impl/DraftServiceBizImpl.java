package com.pig4cloud.pigx.servicebiz.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pig4cloud.pigx.entity.Draft;
import com.pig4cloud.pigx.service.DraftService;
import com.pig4cloud.pigx.servicebiz.DraftServiceBiz;
import com.pig4cloud.pigx.vo.DraftQueryVO;
import com.pig4cloud.pigx.vo.DraftSaveVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 草稿管理表，用于管理用户草稿数据
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
@Slf4j
@AllArgsConstructor
@Service
public class DraftServiceBizImpl implements DraftServiceBiz {

    private final DraftService draftService;

    /**
     * 保存或更新草稿
     * 如果草稿已存在则更新，否则插入新草稿
     */
    @Override
    public boolean saveOrUpdateDraft(DraftSaveVO saveVO) {
        // 1. 查询是否已存在草稿
        Draft existingDraft = draftService.lambdaQuery()
                .eq(Draft::getUserId, saveVO.getUserId())
                .eq(Draft::getDraftType, saveVO.getDraftType())
                .one();

        if (existingDraft == null) {
            // 2. 草稿不存在，创建新草稿
            Draft newDraft = new Draft();
            newDraft.setUserId(saveVO.getUserId());
            newDraft.setDraftType(saveVO.getDraftType());
            newDraft.setDraftContent(saveVO.getDraftContent());
            newDraft.setStatus(0);
            return draftService.save(newDraft);
        } else {
            // 3. 草稿已存在，更新草稿内容
            existingDraft.setDraftContent(saveVO.getDraftContent());
            existingDraft.setStatus(0);
            return draftService.updateById(existingDraft);
        }
    }

    /**
     * 提交草稿
     * 更新草稿状态为已提交
     */
    @Override
    public boolean submitDraft(DraftSaveVO saveVO) {
        // 1. 查询草稿是否存在
        Draft existingDraft = draftService.lambdaQuery()
                .eq(Draft::getUserId, saveVO.getUserId())
                .eq(Draft::getDraftType, saveVO.getDraftType())
                .eq(Draft::getStatus, 0)
                .one();

        if (existingDraft != null) {
            // 2. 更新草稿状态为已提交
            existingDraft.setStatus(1);
            return draftService.updateById(existingDraft);
        }

        return false;
    }

    /**
     * 查询草稿
     *
     * @param queryVO 查询条件
     * @return 草稿
     */
    @Override
    public Draft getDraft(DraftQueryVO queryVO) {
        return draftService.lambdaQuery()
                .eq(Draft::getUserId, queryVO.getUserId())
                .eq(Draft::getDraftType, queryVO.getDraftType())
                .eq(Draft::getStatus, 0)
                .one();
    }
}