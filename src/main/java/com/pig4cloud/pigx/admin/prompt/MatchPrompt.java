package com.pig4cloud.pigx.admin.prompt;

public class MatchPrompt {
    public static final String VALUE = "请作为一名供需智能匹配专家，基于以下【供给信息】与【需求信息】，综合内容相关性、技术方向、应用场景、创新点、合作意愿等多维度，评估其匹配度（0~100分，越高越匹配），并详细说明评分理由。\n" +
            "严格以如下 JSON 格式输出分析结果，不要有多余解释，不要有任何注释，不要缺字段。" +
            "\n" +
            "请输出标准 JSON，包含如下字段：\n" +
            "- match_score：匹配度分数，int，0~100\n" +
            "- match_reason：详细匹配说明（基于供需双方内容，逻辑清晰，体现打分依据）\n" +
            "- supply_summary：对供给信息的简要总结\n" +
            "- demand_summary：对需求信息的简要总结\n" +
            "- related_keywords：主要的相关关键词数组\n" +
            "- advice（可选）：如匹配度不高，建议双方可提升匹配的方向或补充要素\n" +
            "\n" +
            "【供给信息】\n" +
            "{}\n" +
            "\n" +
            "【需求信息】\n" +
            "{}\n";
}
