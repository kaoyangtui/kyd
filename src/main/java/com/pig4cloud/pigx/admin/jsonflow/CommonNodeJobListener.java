package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.listener.NodeJobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
public class CommonNodeJobListener implements NodeJobListener {
    @Override
    public void completeJob(RunJobVO runJobVO) {
        log.info("CommonNodeJobListener.completeJob:{}", JSONUtil.toJsonStr(runJobVO));
    }

    @Override
    public void completeNode(RunNodeVO runNodeVO) {
        log.info("CommonNodeJobListener.completeNode:{}", JSONUtil.toJsonStr(runNodeVO));
    }
}
