
package com.pig4cloud.pigx.jsonflow.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 运行流程 Feign
 *
 * @author luolin
 */
@FeignClient(contextId = "remoteRunFlowService", value = ServiceNameConstants.JSONFLOW_SERVICE)
public interface RemoteRunFlowService {

	/**
	 * 发起流程
	 * @param order 工单数据
	 * @param params 流程条件与分配参与者参数
	 */
	@PostMapping("/run-flow/start")
	R<Boolean> startFlow(@RequestBody Object order, @RequestParam Map<String, Object> params);

	/**
	 * 作废流程
	 * @param runJobVO 运行任务
	 */
	@DeleteMapping("/run-flow/invalid")
	R<Boolean> invalidFlow(@RequestBody RunJobVO runJobVO);

	/**
	 * 根据状态查询
	 * @param flowKey 业务KEY
	 * @param status 状态
	 */
	@GetMapping("/run-flow/key-status/{flowKey}/{status}")
	R<List<RunFlow>> listRunFlows(@PathVariable("flowKey") String flowKey, @PathVariable("status") String status);

	/**
	 * 根据流程ID查询
	 * @param id 流程ID
	 * @param from 是否内部
	 */
	@GetMapping("/run-flow/inner/{id}")
	R<RunFlow> innerGetById(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM) String from);

}
