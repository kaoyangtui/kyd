
package com.pig4cloud.pigx.jsonflow.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 流程参数 Feign
 *
 * @author luolin
 */
@FeignClient(contextId = "remoteFlowVariableService", value = ServiceNameConstants.JSONFLOW_SERVICE)
public interface RemoteFlowVariableService {

	/**
	 * 更新流程参数
	 * @param params 流程参数
	 * @return R
	 */
	@PutMapping("/flow-variable/update")
	R<Boolean> updateFlowVariables(@RequestParam Map<String, Object> params);

}
