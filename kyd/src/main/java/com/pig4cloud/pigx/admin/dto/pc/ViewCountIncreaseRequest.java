package com.pig4cloud.pigx.admin.dto.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用浏览量自增请求")
public class ViewCountIncreaseRequest {

    @Schema(description = "业务类型：\n" +
            "资产资讯: ASSET_NEWS\n" +
            "资产政策: ASSET_POLICY\n" +
            "企业需求: DEMAND\n" +
            "校内需求: DEMAND_IN\n" +
            "活动会议: EVENT_MEETING\n" +
            "知识产权转化: IP_TRANSFORM\n" +
            "专利信息: PATENT_INFO\n" +
            "科研动态: RESEARCH_NEWS\n" +
            "科研团队: RESEARCH_TEAM\n" +
            "科研成果: RESULT\n" +
            "转化案例: TRANSFORM_CASE")
    private String bizCode;

    @Schema(description = "业务主键ID")
    private Long bizId;
}
