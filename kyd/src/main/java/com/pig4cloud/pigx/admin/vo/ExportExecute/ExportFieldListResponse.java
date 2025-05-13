package com.pig4cloud.pigx.admin.vo.ExportExecute;

import lombok.Data;

import java.util.List;

@Data
public class ExportFieldListResponse {
    private String bizCode;
    private List<ExportFieldResponse> fields;
}
