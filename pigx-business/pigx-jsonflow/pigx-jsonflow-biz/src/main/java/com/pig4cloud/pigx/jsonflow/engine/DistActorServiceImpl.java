package com.pig4cloud.pigx.jsonflow.engine;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 钉钉函数审批人/角色接口实现
 * @author luiolin
 * @date 2023/05/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistActorServiceImpl implements IDistActorService{

	private final static int MAX_LEADER_LEVEL = 20;

	private final IAuditorService iAuditorService;

	private SysUser getSysUser(Long whoseLeader) {
		return iAuditorService.getUserByUserId(whoseLeader);
	}

	private SysDept getSysDept(Long userId) {
		return iAuditorService.getDeptByDeptId(userId);
	}

	private List<Long> getLeaderIds(SysDept sysDept) {
		return iAuditorService.getDeptAllLeader(sysDept.getDeptId());
	}

	@Override
	public List<DistPerson> getUserDeptLeaderId(Long whoseLeader, Integer leaderLevel, String levelExtract) {
		SysUser sysUser = this.getSysUser(whoseLeader);
		SysDept sysDept = this.getSysDept(sysUser.getDeptId());
		for (int i = 2; i <= leaderLevel; i++) {
			sysDept = this.getSysDept(sysDept.getParentId());
			if (Objects.isNull(sysDept)) break;
		}
		List<Long> leaderIds = null;
		if (CommonNbrPool.STR_0.equals(levelExtract) && Objects.nonNull(sysDept)) {
			leaderIds = this.getLeaderIds(sysDept);
			if (CollUtil.isEmpty(leaderIds)) {
				for (int i = leaderLevel + 1; i <= MAX_LEADER_LEVEL; i++) {
					sysDept = this.getSysDept(sysDept.getParentId());
					if (Objects.isNull(sysDept)) break;
					leaderIds = this.getLeaderIds(sysDept);
					if (CollUtil.isNotEmpty(leaderIds)) break;
				}
			}
		}
		if (CollUtil.isEmpty(leaderIds)) throw new ValidationException("KEY取值来源远程调用返回单级部门主管不存在");
		return this.buildDistPerson(leaderIds);
	}

	private List<DistPerson> buildDistPerson(List<Long> leaderIds) {
		List<DistPerson> distPersons = new ArrayList<>();
		leaderIds.forEach(userId -> {
			DistPerson distPerson = new DistPerson();
			distPerson.setRoleId(userId);
			distPerson.setJobType(JobUserTypeEnum.USER.getType());
			distPersons.add(distPerson);
		});
		return distPersons;
	}

	@Override
	public List<DistPerson> listUserDeptMultiLeaderId(Long whoseLeader, String auditEndpoint, Integer leaderLevel, String levelExtract) {
		List<Long> userIds = new ArrayList<>();
		SysUser sysUser = this.getSysUser(whoseLeader);
		SysDept sysDept = this.getSysDept(sysUser.getDeptId());
		List<Long> leaderIds = this.getLeaderIds(sysDept);
		if (CollUtil.isNotEmpty(leaderIds)) userIds.addAll(leaderIds);
		if (CommonNbrPool.STR_0.equals(auditEndpoint)) {
			while (true) {
				sysDept = this.getSysDept(sysDept.getParentId());
				if (Objects.isNull(sysDept)) break;
				leaderIds = this.getLeaderIds(sysDept);
				if (CollUtil.isNotEmpty(leaderIds)) userIds.addAll(leaderIds);
			}
		} else {
			for (int i = 2; i <= leaderLevel; i++) {
				sysDept = this.getSysDept(sysDept.getParentId());
				if (Objects.isNull(sysDept)) break;
				leaderIds = this.getLeaderIds(sysDept);
				if (CollUtil.isNotEmpty(leaderIds)) userIds.addAll(leaderIds);
			}
			if (CommonNbrPool.STR_0.equals(levelExtract) && Objects.nonNull(sysDept) && CollUtil.size(userIds) != leaderLevel) {
				for (int i = leaderLevel + 1; i <= MAX_LEADER_LEVEL; i++) {
					sysDept = this.getSysDept(sysDept.getParentId());
					if (Objects.isNull(sysDept)) break;
					leaderIds = this.getLeaderIds(sysDept);
					if (CollUtil.isNotEmpty(leaderIds)) userIds.addAll(leaderIds);
					if (CollUtil.size(userIds) == leaderLevel) break;
				}
			}
		}
		if (CollUtil.isEmpty(userIds)) throw new ValidationException("KEY取值来源远程调用返回多级部门主管不存在");
		return this.buildDistPerson(userIds);
	}

	@Override
	public List<DistPerson> getDeptLeaderId(Long appointDeptId, String levelExtract) {
		SysDept sysDept = this.getSysDept(appointDeptId);
		List<Long> leaderIds = this.getLeaderIds(sysDept);
		if (CommonNbrPool.STR_0.equals(levelExtract) && CollUtil.isEmpty(leaderIds)) {
			for (int i = 2; i <= MAX_LEADER_LEVEL; i++) {
				sysDept = this.getSysDept(sysDept.getParentId());
				if (Objects.isNull(sysDept)) break;
				leaderIds = this.getLeaderIds(sysDept);
				if (CollUtil.isNotEmpty(leaderIds)) break;
			}
		}
		if (CollUtil.isEmpty(leaderIds)) throw new ValidationException("KEY取值来源远程调用返回指定部门主管不存在");
		return this.buildDistPerson(leaderIds);
	}

}
