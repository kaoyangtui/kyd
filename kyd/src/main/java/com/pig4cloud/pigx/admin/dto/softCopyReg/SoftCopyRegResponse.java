package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.pig4cloud.pigx.admin.entity.SoftCopyRegCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegOwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 软著登记响应
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "软著登记响应")
public class SoftCopyRegResponse {

    @Schema(description = "主表信息")
    private SoftCopyRegMainResponse main;

    @Schema(description = "著作权人列表")
    private List<SoftCopyRegOwnerEntity> owners;

    @Schema(description = "完成人列表")
    private List<SoftCopyRegCompleterEntity> completers;

    public static final String BIZ_CODE = "soft_copy_reg_list";
}
