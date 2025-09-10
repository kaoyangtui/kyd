package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JfRunFlowServiceImpl implements JfRunFlowService {

    private final JfRunFlowMapper jfRunFlowMapper;
    private final RunJobService runJobService;

    @Override
    @SneakyThrows
    @Transactional(readOnly = true)
    public Page<MyFlowResponse> pageFlowList(Page<MyFlowResponse> page, MyFlowRequest req) {
        return jfRunFlowMapper.page(page, req);
    }

    @Override
    public Long getToDoneCount(ToDoneJobVO toDoneJobVO) {
        QueryWrapper<RunJob> query = this.copyRunJobDoneQueryWrapper(toDoneJobVO);

        // 状态筛选
        List<String> statuses = CollUtil.newArrayList(NodeJobStatusEnum.COMPLETE.getStatus());
        if (CommonNbrPool.STR_1.equals(toDoneJobVO.getQueryJobType())) {
            statuses = CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
        }
        if (StrUtil.isNotBlank(toDoneJobVO.getStatus())) {
            statuses = CollUtil.newArrayList(toDoneJobVO.getStatus());
        }
        if (CollUtil.isNotEmpty(statuses)) {
            query.lambda().in(RunJob::getStatus, statuses);
        }
        return runJobService.count(query);
    }


    /**
     * 对象转换
     *
     * @param toDoneJobVO 已办任务
     */
    private QueryWrapper<RunJob> copyRunJobDoneQueryWrapper(ToDoneJobVO toDoneJobVO) {
        RunJob runJob = new RunJob();
        BeanUtil.copyProperties(toDoneJobVO, runJob);
        runJob.setSignatureType(null);
        runJob.setBelongType(null);
        return Wrappers.query(runJob);
    }

}
