package com.pig4cloud.pigx.admin.jsonflow;

import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

public interface FlowStatusUpdater {
    String flowKey();
    void update(FlowStatusUpdateDTO flowStatusUpdateDTO);
}
