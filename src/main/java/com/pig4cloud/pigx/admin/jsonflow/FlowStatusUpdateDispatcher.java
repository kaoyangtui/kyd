package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import lombok.RequiredArgsConstructor;
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

    public void dispatch(RunNodeVO vo) {
        FlowStatusUpdater updater = updaterMap.get(vo.getFlowKey());
        if (updater != null) {
            updater.update(vo);
        }
    }
}
