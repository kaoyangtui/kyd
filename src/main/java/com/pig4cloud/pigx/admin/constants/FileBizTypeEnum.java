package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 附件类型
 *
 * @author zhaoliang
 */
@Getter
public enum FileBizTypeEnum {
    ATTACHMENT("", "附件"),
    PATENT_PROPOSAL_CLAIMS("专利提案", "权利要求书"),
    PATENT_PROPOSAL_DESCRIPTION("专利提案", "说明书"),
    PATENT_PROPOSAL_ABSTRACT("专利提案", "说明书摘要"),
    IP_ASSIGN_PROOF("赋权", "其它发明人同意证明"),
    IP_ASSIGN_ATTACH("赋权", "赋权申请"),
    IP_TRANSFORM_CONSENT("知识产权转化", "其它发明人同意证明"),
    IP_TRANSFORM_PROMISE("知识产权转化", "专利转化承诺书"),
    IP_TRANSFORM_CONTRACT("知识产权转化", "专利转化合同"),
    IP_TRANSFORM_REWARD_APPLY("知识产权转化", "专利转化收入奖励的申请"),
    IP_TRANSFORM_ALLOCATION_PLAN("知识产权转化", "专利转化收入的分配方案"),
    IP_TRANSFORM_RECORD("知识产权转化", "火炬中心备案文件"),
    IC_LAYOUT_CERT("集成电路布图登记", "证书"),
    PLANT_VARIETY_CERT("植物新品种权登记", "证书"),
    SOFT_COPY_CERT("软著登记", "证书")
    ;

    private final String bizName;
    private final String value;


    FileBizTypeEnum(String bizName, String value) {
        this.bizName = bizName;
        this.value = value;
    }
}
