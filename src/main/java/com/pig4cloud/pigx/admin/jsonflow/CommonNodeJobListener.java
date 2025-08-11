package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.listener.GlobalFlowListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonNodeJobListener implements GlobalFlowListener {

    private final FlowStatusUpdateDispatcher dispatcher;

    private void handle(RunJobVO runJobVO) {
        FlowStatusUpdateDTO dto = new FlowStatusUpdateDTO();
        dto.setFlowInstId(String.valueOf(runJobVO.getFlowInstId()));
        dto.setFlowKey(runJobVO.getFlowKey());
        boolean ifUpdate = false;
        if (StrUtil.isNotBlank(runJobVO.getFlowStatus())) {
            dto.setFlowStatus(Integer.valueOf(runJobVO.getFlowStatus()));
            ifUpdate = true;
        }
        if (StrUtil.isNotBlank(runJobVO.getNodeName())) {
            dto.setCurrentNodeName(runJobVO.getNodeName());
            ifUpdate = true;
        }
        if (ifUpdate) {
            dispatcher.dispatch(dto);
        }
    }

    /**
     * 当完成节点时
     * @param runNodeVO 运行节点
     */
    @Override
    public void completeNode(RunNodeVO runNodeVO) {
        log.info("*****************CommonNodeJobListener.completeNode:{}", JSONUtil.toJsonStr(runNodeVO));
        this.handle(runNodeVO);
    }

    /**
     * 当流程发起时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void initiate(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.initiate:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程完成时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void finish(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.finish:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程被撤回时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void recall(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.recall:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程被重发时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void reset(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.reset:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程被终止时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void terminate(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.terminate:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程被作废时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void invalid(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.invalid:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }

    /**
     * 当流程被恢复时
     *
     * @param runJobVO 运行任务
     */
    @Override
    public void recover(RunJobVO runJobVO) {
        log.info("*****************CommonNodeJobListener.recover:{}", JSONUtil.toJsonStr(runJobVO));
        this.handle(runJobVO);
    }
}
