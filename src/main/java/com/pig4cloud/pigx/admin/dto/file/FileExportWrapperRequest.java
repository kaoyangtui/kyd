package com.pig4cloud.pigx.admin.dto.file;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileExportWrapperRequest extends ExportWrapperRequest<FilePageRequest> {

}