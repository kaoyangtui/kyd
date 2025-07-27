package com.pig4cloud.pigx.admin.constants;

/**
 * 消息内容模板枚举
 */
public enum MessageTemplateEnum {

    /**
     * 流程提醒
     * 示例：您提交的【风力发电机专利】流程已“审核通过”，请关注后续进展。
     */
    FLOW_REMINDER("您提交的【{}】流程已{}，请关注后续进展。", "流程提醒"),

    /**
     * 专利监控-法律状态变化
     * 示例：监控的专利【风力发电机】法律状态信息发生变更，请知悉。
     */
    PATENT_MONITOR_STATUS_CHANGE("监控的专利【{}】法律状态信息发生变更，请知悉。", "专利监控-法律状态"),

    /**
     * 专利监控-详细变更
     * 示例：监控的专利【风力发电机】法律状态变更为【已审】，请知悉。
     */
    PATENT_MONITOR_STATUS_DETAIL("监控的专利【{}】法律状态变更为【{}】，请知悉。", "专利监控提醒"),

    /**
     * 转化专利监控
     * 示例：转化专利【风力发电机】权利人信息已变更为【新能源公司】，请知悉。
     */
    TRANSFER_MONITOR_CHANGE("转化专利【{}】权利人信息已变更为【{}】，请知悉。", "转化专利监控"),

    /**
     * 需求接收提醒
     * 示例：收到一个需求推送【风电技术合作】，请及时处理。
     */
    DEMAND_RECEIVE("收到一个需求推送【{}】，请及时处理。", "需求接收提醒"),

    /**
     * 活动报名提醒
     * 示例：活动会议【专利研讨会】收到一个报名，请知悉。
     */
    ACTIVITY_SIGNUP("活动会议【{}】收到一个报名，请知悉。", "活动报名提醒"),

    /**
     * 系统通知
     * 示例：您的账号密码已被重置，请及时修改。
     */
    SYSTEM_NOTICE("【系统通知】{}", "系统通知"),

    /**
     * 其它
     */
    OTHER("{}", "其它");

    private final String template;
    private final String description;

    MessageTemplateEnum(String template, String description) {
        this.template = template;
        this.description = description;
    }

    public String getTemplate() {
        return template;
    }

    public String getDescription() {
        return description;
    }
}
