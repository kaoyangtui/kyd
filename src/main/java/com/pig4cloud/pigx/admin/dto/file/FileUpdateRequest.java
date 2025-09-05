package com.pig4cloud.pigx.admin.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileUpdateRequest extends FileCreateRequest {

    @Schema(description = "主键")
    private Long id;

}