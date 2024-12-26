package com.pig4cloud.pigx.servicebiz;

import com.pig4cloud.pigx.entity.Draft;
import com.pig4cloud.pigx.vo.DraftQueryVO;
import com.pig4cloud.pigx.vo.DraftSaveVO;

/**
 * 草稿管理表，用于管理用户草稿数据
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
public interface DraftServiceBiz {


    /**
     * 保存或更新草稿
     *
     * @param saveVO 草稿保存数据
     * @return 是否保存成功
     */
    boolean saveOrUpdateDraft(DraftSaveVO saveVO);

    /**
     * 提交草稿
     *
     * @param saveVO 草稿提交数据
     * @return 是否提交成功
     */
    boolean submitDraft(DraftSaveVO saveVO);

    /**
     * 查询草稿
     *
     * @param queryVO 查询条件
     * @return 草稿数据
     */
    Draft getDraft(DraftQueryVO queryVO);
}