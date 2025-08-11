package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        }
    }
}
