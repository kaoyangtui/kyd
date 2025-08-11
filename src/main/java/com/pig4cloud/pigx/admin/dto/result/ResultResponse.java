package com.pig4cloud.pigx.admin.dto.result;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "科研成果返回信息")
public class ResultResponse extends BaseResponse {

    public static final String BIZ_CODE = "RESULT";

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 成果编码，CG开头+雪花算法ID
     */
    @Schema(description = "成果编码，CG开头+雪花算法ID")
    private String code;

    /**
     * 流程实例 ID
     */
    @Schema(description = "流程实例 ID")
    private String flowInstId;

    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY")
    private String flowKey;

    /**
     * 流程状态：-2暂存 -1运行中 0完成 1作废 2撤回
     */
    @Schema(description = "流程状态：-2暂存 -1运行中 0完成 1作废 2撤回")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    /**
     * 成果名称
     */
    @Schema(description = "成果名称")
    private String name;

    /**
     * 所属学科
     */
    @Schema(description = "所属学科")
    private String subject;

    /**
     * 领域技术
     */
    @Schema(description = "技术领域")
    private List<String> techArea;

    /**
     * 研究方向
     */
    @Schema(description = "研究方向")
    private String direction;

    /**
     * 是否依托项目，0否1是
     */
    @Schema(description = "是否依托项目，0否1是")
    private Integer fromProj;


    @Schema(description = "项目ID")
    private Long researchProjectId;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    private String researchProjectType;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String researchProjectName;

    /**
     * 成果简介
     */
    @Schema(description = "成果简介")
    private String intro;

    /**
     * 技术成熟度
     */
    @Schema(description = "技术成熟度")
    private String maturity;

    /**
     * 是否有实物，0否1是
     */
    @Schema(description = "是否有实物，0否1是")
    private Integer hasObj;

    /**
     * 转化方式，多选用;分隔
     */
    @Schema(description = "转化方式，多选用;分隔")
    private List<String> transWay;

    /**
     * 转化价格(万元)
     */
    @Schema(description = "转化价格(万元),-1为面议")
    private BigDecimal transPrice;

    /**
     * 评价或获奖情况
     */
    @Schema(description = "评价或获奖情况")
    private String award;

    /**
     * 成果图片URL
     */
    @Schema(description = "成果图片URL")
    private List<String> imgUrl;

    /**
     * 附件URL，多个
     */
    @Schema(description = "附件URL，多个")
    private List<String> fileUrl;

    /**
     * 上下架状态，0下架1上架
     */
    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;

    /**
     * 上下架时间
     */
    @Schema(description = "上下架时间")
    private LocalDateTime shelfTime;

    /**
     * 关键词
     */
    @Schema(description = "关键词")
    private String tags;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "完成人信息列表")
    private List<CompleterEntity> completers;

    @Schema(description="匹配度分数 0-100")
    private Integer matchScore;
}
