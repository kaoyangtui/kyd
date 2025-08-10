package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.listener.NodeJobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonNodeJobListener implements NodeJobListener {

    private final FlowStatusUpdateDispatcher dispatcher;

    @Override
    public void completeNode(RunNodeVO runNodeVO) {
        log.info("CommonNodeJobListener.completeNode:{}", JSONUtil.toJsonStr(runNodeVO));
        dispatcher.dispatch(runNodeVO);
    }
}
