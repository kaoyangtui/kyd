
package com.pig4cloud.pigx.jsonflow.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 运行任务 Feign
 *
 * @author luolin
 */
@FeignClient(contextId = "remoteRunJobService", value = ServiceNameConstants.JSONFLOW_SERVICE)
public interface RemoteRunJobService {

	/**
	 * 根据任务ID设置办理人
	 * @param runJobs 运行任务
	 * @param handoverUser 交接人
	 * @param handoverReason 交接原因
	 * @param type 类型
	 */
	@PutMapping("/run-job/handover/ids")
	R<Boolean> handleJobHandover(@RequestBody List<RunJob> runJobs, @RequestParam("handoverUser") Long handoverUser,
			@RequestParam("handoverReason") String handoverReason, @RequestParam("type") String type);

}
