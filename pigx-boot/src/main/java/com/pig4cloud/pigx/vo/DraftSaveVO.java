package com.pig4cloud.pigx.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author zhaoliang
 */
@Data
public class DraftSaveVO {

    /**
     * 用户ID，不能为空
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 草稿类型，不能为空
     */
    @NotNull(message = "草稿类型不能为空")
    private String draftType;

    /**
     * 草稿内容，用户填写的具体草稿内容
     */
    private String draftContent;
}
