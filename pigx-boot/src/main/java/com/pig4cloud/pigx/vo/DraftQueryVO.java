package com.pig4cloud.pigx.vo;

import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
public class DraftQueryVO {
    /**
     * 用户ID，用于查询该用户的草稿
     */
    private Long userId;

    /**
     * 草稿类型，表示不同类型的草稿
     */
    private String draftType;
}
