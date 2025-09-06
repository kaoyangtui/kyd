package com.pig4cloud.pigx.admin.jsonflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;

public interface JfRunFlowService {
    Page<MyFlowResponse> pageFlowList(Page<MyFlowResponse> page, MyFlowRequest req);

    Long getToDoneCount(ToDoneJobVO toDoneJobVO);
}
