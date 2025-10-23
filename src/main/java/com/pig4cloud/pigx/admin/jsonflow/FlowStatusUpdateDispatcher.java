package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FlowStatusUpdateDispatcher {

    private final Map<String, FlowStatusUpdater> updaterMap;

    public FlowStatusUpdateDispatcher(java.util.List<FlowStatusUpdater> updaters) {
        this.updaterMap = CollUtil.isEmpty(updaters)
                ? java.util.Collections.emptyMap()
                : updaters.stream().collect(Collectors.toMap(FlowStatusUpdater::flowKey, Function.identity(), (a,b)->a));
    }

    public void dispatch(FlowStatusUpdateDTO flowStatusUpdateDTO) {
        FlowStatusUpdater updater = updaterMap.get(flowStatusUpdateDTO.getFlowKey());
        if (updater != null) {
            updater.update(flowStatusUpdateDTO);
        } else {
            log.error("!!!!!!!!!!!!!!!!未找到流程状态更新器，流程KEY：{}", flowStatusUpdateDTO.getFlowKey());
        }
    }
}
