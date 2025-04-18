package com.pig4cloud.pigx.admin.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description: 专利搜索列表请求
 * @author: XX
 * @time: 2023/5/8
 */
@Data
@Schema(description = "专利搜索列表请求")
public class PatentSearchListReq {

    @Schema(description = "当前页")
    private int from = 1;

    @Schema(description = "每页条数")
    private int size = 10;

    @Schema(description = "专利名称")
    private String patentTitle;

    @Schema(description = "申请号")
    private String appNumber;

    @Schema(description = "摘要")
    private String abs;

    @Schema(description = "专利类型")
    private List<String> patentTypeArray;

    @Schema(description = "法律状态")
    private List<String> legalStatusArray;

    @Schema(description = "分类号部")
    private List<String> ipcSectionArray;

    @Schema(description = "分类号大类")
    private List<String> ipcClassArray;

    @Schema(description = "分类号小类")
    private List<String> ipcSubClassArray;

    @Schema(description = "分类号")
    private List<String> ipcArray;

    @Schema(description = "专利申请日；日期段数组：索引0：开始日期，索引1：结束日期；格式yyyy-MM-dd")
    private String[] applicationDateRange;

    @Schema(description = "公开（公告）日；日期段数组：索引0：开始日期，索引1：结束日期；格式yyyy-MM-dd")
    private String[] publicationDateRange;

    @Schema(description = "申请（专利权）人")
    private String currentApplicantOrOwner;

    @Schema(description = "发明（设计）人")
    private String inventor;

    @Schema(description = "是否高价值,0否1是")
    private Integer highValueFlag;

    @Schema(description = "高价值项,战略新兴产业标识、海外同族、维持年限超十年、质押融资、获奖")
    private List<String> highValueArray;

    @Schema(description = "排序类型，1-按申请日倒序 2-按申请人顺序")
    private Integer sortType;

}
