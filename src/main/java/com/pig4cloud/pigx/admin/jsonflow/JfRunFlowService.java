package com.pig4cloud.pigx.admin.jsonflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface JfRunFlowService {
    Page<MyFlowResponse> pageFlowList(Page<MyFlowResponse> page, MyFlowRequest req);
}
