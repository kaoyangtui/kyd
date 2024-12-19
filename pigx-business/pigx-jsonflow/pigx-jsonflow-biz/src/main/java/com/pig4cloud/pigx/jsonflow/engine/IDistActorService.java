package com.pig4cloud.pigx.jsonflow.engine;

import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;

import java.util.List;

/**
 * 钉钉函数审批人/角色接口实现
 * @author luiolin
 * @date 2023/05/20
 */
public interface IDistActorService {

	/**
	 * 获取单级部门主管
	 * @param whoseLeader 谁的主管
	 * @param leaderLevel 层级
	 * @param levelExtract 提取规则
	 * @return Long
	 */
	List<DistPerson> getUserDeptLeaderId(Long whoseLeader, Integer leaderLevel, String levelExtract);

	/**
	 * 获取多级部门主管
	 * @param whoseLeader 谁的主管
	 * @param auditEndpoint 审批终点
	 * @param leaderLevel 层级
	 * @param levelExtract 提取规则
	 * @return List<Long>
	 */
	List<DistPerson> listUserDeptMultiLeaderId(Long whoseLeader, String auditEndpoint, Integer leaderLevel, String levelExtract);

	/**
	 * 获取指定部门主管
	 * @param appointDeptId 指定部门ID
	 * @param levelExtract 提取规则
	 * @return Long
	 */
	List<DistPerson> getDeptLeaderId(Long appointDeptId, String levelExtract);

}
