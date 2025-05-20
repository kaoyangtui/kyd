package com.pig4cloud.pigx.admin.vo.ipTransform;

import com.pig4cloud.pigx.admin.vo.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分页请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "知识产权转化分页查询")
public class IpTransformPageRequest extends BasePageQuery {

    @Schema(description = "关键词：转化项目名称或编码")
    private String keyword;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "所属院系")
    private String createByDept;

    @Schema(description = "提交时间-起")
    private String beginTime;

    @Schema(description = "提交时间-止")
    private String endTime;

}