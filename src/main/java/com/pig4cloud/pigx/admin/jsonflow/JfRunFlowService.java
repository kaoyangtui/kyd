package com.pig4cloud.pigx.admin.jsonflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface JfRunFlowService {
    Page<FlowListResponse> pageFlowList(Page<FlowListResponse> page, FlowListPageRequest req);
}
