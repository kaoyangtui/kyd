package com.pig4cloud.pigx.admin.dto.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用浏览量自增请求")
public class ViewCountIncreaseRequest {

    @Schema(description = """
            业务类型：
            资产资讯: ASSET_NEWS
            资产政策: ASSET_POLICY
            企业需求: DEMAND
            校内需求: DEMAND_IN
            活动会议: EVENT_MEETING
            知识产权转化: IP_TRANSFORM
            专利信息: PATENT_INFO
            科研动态: RESEARCH_NEWS
            科研团队: RESEARCH_TEAM
            科研成果: RESULT
            转化案例: TRANSFORM_CASE""")
    private String bizCode;

    @Schema(description = "业务主键ID")
    private Long bizId;
}
