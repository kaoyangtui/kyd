package com.pig4cloud.pigx.admin.dto.platformInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "关于平台内容修改请求")
public class PlatformInfoUpdateRequest extends PlatformInfoCreateRequest{

}
