package com.pig4cloud.pigx.admin.dto.result;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 科研成果分页查询请求
 * 继承通用分页能力
 *
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "科研成果分页查询请求")
public class ResultPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持名称、编码模糊查询）")
    private String keyword;

    @Schema(description = "完成人学工号")
    private String completerNo;

    @Schema(description = "负责人用户名")
    private String leaderCode;

    @Schema(description = "提交人用户名")
    private String createBy;

    @Schema(description = "所属学科")
    private String subject;

    @Schema(description = "所属院系 ID")
    private String createByDept;

    @Schema(description = "上下架状态（0 下架，1 上架）")
    private Integer shelfStatus;

    @Schema(description = "流程状态（-1未开始，0办理中，1结束，2驳回中，3跳过，9被驳回）")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "创建时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "创建时间止（yyyy-MM-dd）")
    private String endTime;

    @Schema(description = "技术成熟度")
    private List<String> maturity;

    @Schema(description = "领域技术")
    private List<String> techArea;

    @Schema(description = "合作方式")
    private List<String> transWay;
}
