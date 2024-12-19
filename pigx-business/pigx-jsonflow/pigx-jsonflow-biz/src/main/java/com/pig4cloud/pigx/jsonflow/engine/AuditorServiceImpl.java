package com.pig4cloud.pigx.jsonflow.engine;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteJsonFlowService;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.vo.UserRolePickerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审批人/角色接口实现
 * @author luiolin
 * @date 2023/05/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditorServiceImpl implements IAuditorService{

	private final RemoteJsonFlowService remoteJsonFlowService;

	@Override
	public List<SysUser> listUsersByUserIds(List<Long> userIds) {
		return remoteJsonFlowService.listUsersByUserIds(userIds, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public SysUser getUserByUserId(Long userId) {
		return remoteJsonFlowService.getByUserId(userId, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public List<SysPost> listPostsByUserId(Long userId) {
		return remoteJsonFlowService.listPostsByUserId(userId, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public SysDept getDeptByDeptId(Long deptId) {
		return remoteJsonFlowService.getByDeptId(deptId).getData();
	}

	@Override
	public List<Long> getDeptAllLeader(Long deptId) {
		return remoteJsonFlowService.getDeptAllLeader(deptId).getData();
	}

	@Override
	public List<SysUser> listUsersByRoleId(Long roleId, String jobType) {
		if (JobUserTypeEnum.USER.getType().equals(jobType)) {
			SysUser sysUser = this.getUserByUserId(roleId);
			return CollUtil.newArrayList(sysUser);
		} else if (JobUserTypeEnum.ROLE.getType().equals(jobType)) {
			return remoteJsonFlowService.listUsersByRoleId(roleId, SecurityConstants.FROM_IN).getData();
		} else if (JobUserTypeEnum.POST.getType().equals(jobType)) {
			return remoteJsonFlowService.listUsersByPostId(roleId, SecurityConstants.FROM_IN).getData();
		} else if (JobUserTypeEnum.DEPT.getType().equals(jobType)) {
			return remoteJsonFlowService.listUsersByDeptId(roleId, SecurityConstants.FROM_IN).getData();
		}
		throw new ValidationException("当前任务不存在指定的参与者类型，请核实");
	}

	@Override
	public List<UserVO> listUsersByRoleIds(List<Long> roleIds, String jobType) {
		if (JobUserTypeEnum.USER.getType().equals(jobType)) {
			List<SysUser> sysUsers = this.listUsersByUserIds(roleIds);
			return sysUsers.stream().map(map -> {
				UserVO userVO = new UserVO();
				BeanUtil.copyProperties(map, userVO);
				return userVO;
			}).collect(Collectors.toList());
		} else if (JobUserTypeEnum.ROLE.getType().equals(jobType)) {
			return remoteJsonFlowService.listUserRolesByRoleIds(roleIds, SecurityConstants.FROM_IN).getData();
		} else if (JobUserTypeEnum.POST.getType().equals(jobType)) {
			return remoteJsonFlowService.listUserPostsByPostIds(roleIds, SecurityConstants.FROM_IN).getData();
		} else if (JobUserTypeEnum.DEPT.getType().equals(jobType)) {
			return remoteJsonFlowService.listUserDeptsByDeptIds(roleIds, SecurityConstants.FROM_IN).getData();
		}
		throw new ValidationException("当前任务不存在指定的参与者类型，请核实");
	}

	@Override
	public List<SysRole> listRolesByRoleIds(List<Long> roleIds) {
		return remoteJsonFlowService.listRolesByRoleIds(roleIds, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public List<SysPost> listPostsByPostIds(List<Long> postIds) {
		return remoteJsonFlowService.listPostsByPostIds(postIds, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public List<SysDept> listDeptsByDeptIds(List<Long> deptIds) {
		return remoteJsonFlowService.listDeptsByDeptIds(deptIds, SecurityConstants.FROM_IN).getData();
	}

	@Override
	public UserRolePickerVO<Long> fetchUserRolePicker() {
		UserRolePickerVO<Long> res = new UserRolePickerVO<>();
		List<Tree<String>> trees = remoteJsonFlowService.getDeptTree(null, null).getData();
		List<SysUser> sysUsers = this.listUsers();
		if (CollUtil.isEmpty(trees) || CollUtil.isEmpty(sysUsers)) return res;
		res.setId(0L);
		res.setName("组织架构");
		this.buildUsersInfo(res, trees, sysUsers);
		return res;
	}

	private void buildUsersInfo(UserRolePickerVO<Long> res, List<Tree<String>> trees, List<SysUser> sysUsers){
		if (CollUtil.isEmpty(trees)) return;
		List<UserRolePickerVO<Long>> vos = new ArrayList<>();
		trees.forEach(each -> {
			UserRolePickerVO<Long> vo = new UserRolePickerVO<>();
			vo.setId(Long.valueOf(each.getId()));
			vo.setParentId(Long.valueOf(each.getParentId()));
			vo.setName(String.valueOf(each.getName()));
			vo.setSort((Integer) each.getWeight());
			List<SysUser> list = sysUsers.stream().filter(f -> f.getDeptId().equals(Long.valueOf(each.getId()))).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(list)) {
				vo.setUsers(list);
				vo.setUsersCount((long) list.size());
			}
			vos.add(vo);
			List<Map<String, Object>> maps = (List)each.getChildren();
			if (CollUtil.isEmpty(maps)) return;
			List<Tree<String>> children = maps.stream().map(map -> {
				Tree<String> tree = new Tree<>();
				BeanUtil.copyProperties(map, tree);
				return tree;
			}).collect(Collectors.toList());
			this.buildUsersInfo(vo, children, sysUsers);
		});
		res.setChildren(vos);
	}

	@Override
	public List<SysUser> listUsersWidthUserIds(List<Long> userIds) {
		if(userIds == null) userIds = CollUtil.newArrayList();
		return remoteJsonFlowService.listUsersWidthUserIds(userIds).getData();
	}

	@Override
	public List<SysRole> listRoles() {
		return remoteJsonFlowService.listRoles().getData();
	}

	@Override
	public List<SysPost> listPosts() {
		return remoteJsonFlowService.listPosts().getData();
	}

	@Override
	public List<SysUser> listUsers() {
		return remoteJsonFlowService.listUsers().getData();
	}

	@Override
	public List<SysDept> listDepts() {
		return remoteJsonFlowService.listDepts().getData();
	}

}
