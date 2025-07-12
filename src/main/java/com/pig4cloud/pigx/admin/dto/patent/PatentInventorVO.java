package com.pig4cloud.pigx.admin.dto.patent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 专利发明人
 *
 * @author pigx
 * @date 2025-05-31 10:46:36
 */
@Data
public class PatentInventorVO {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 发明人顺位
     */
    @Schema(description = "发明人顺位")
    private String priority;

    /**
     * 工号
     */
    @Schema(description = "工号")
    private String code;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 学院/单位
     */
    @Schema(description = "学院/单位")
    private Long deptId;

    /**
     * 单位名称
     */
    @Schema(description = "单位名称")
    private String deptName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactNumber;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String email;

    /**
     * 是否负责人
     */
    @Schema(description = "是否负责人")
    private Integer isLeader;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}