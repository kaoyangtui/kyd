package com.pig4cloud.pigx.admin.jsonflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JfRunFlowServiceImpl implements JfRunFlowService {

    private final JfRunFlowMapper jfRunFlowMapper;

    @Override
    @SneakyThrows
    @Transactional(readOnly = true)
    public Page<FlowListResponse> pageFlowList(Page<FlowListResponse> page, FlowListPageRequest req) {
        return jfRunFlowMapper.page(page, req);
    }
}
