package com.pig4cloud.pigx.jsonflow.engine;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.jsonflow.api.vo.UserRolePickerVO;

import java.util.List;

/**
 * 审批人/角色接口
 * @author luiolin
 * @date 2023/05/20
 */
public interface IAuditorService {

	/**
	 * 批量获取用户
	 * @param userIds 用户ID集合
	 * @return 用户信息
	 */
	List<SysUser> listUsersByUserIds(List<Long> userIds);

	/**
	 * 通过用户ID查询用户
	 *
	 * @param userId 用户id
	 * @return 用户信息
	 */
	SysUser getUserByUserId(Long userId);

	/**
	 * 通过用户ID查询用户岗位
	 *
	 * @param userId 用户id
	 * @return 用户岗位
	 */
	List<SysPost> listPostsByUserId(Long userId);

	/**
	 * 通过部门ID查询用户
	 *
	 * @param deptId 部门id
	 * @return 用户信息
	 */
	SysDept getDeptByDeptId(Long deptId);

	/**
	 * 通过部门ID查询用户领导
	 *
	 * @param deptId 部门id
	 * @return 用户信息
	 */
	List<Long> getDeptAllLeader(Long deptId);

	/**
	 * 通过角色ID查询用户集合
	 *
	 * @param roleId 角色ID
	 * @return 用户信息
	 */
	List<SysUser> listUsersByRoleId(Long roleId, String jobType);

	/**
	 * 通过角色ID查询用户集合
	 *
	 * @param roleIds 角色ID
	 * @return 用户信息
	 */
	List<UserVO> listUsersByRoleIds(List<Long> roleIds, String jobType);

	/**
	 * 批量获取角色
	 * @param roleIds 角色ID集合
	 * @return 角色信息
	 */
	List<SysRole> listRolesByRoleIds(List<Long> roleIds);

	/**
	 * 批量获取岗位
	 * @param postIds 岗位ID集合
	 * @return 岗位信息
	 */
	List<SysPost> listPostsByPostIds(List<Long> postIds);

	/**
	 * 批量获取部门
	 * @param deptIds 部门ID集合
	 * @return 部门信息
	 */
	List<SysDept> listDeptsByDeptIds(List<Long> deptIds);

	/**
	 * 办理人/角色选择器数据
	 */
	UserRolePickerVO<Long> fetchUserRolePicker();

	/**
	 * 查询租户所有用户
	 */
	List<SysUser> listUsersWidthUserIds(List<Long> userIds);

	/**
	 * 获取角色列表
	 * @return 角色信息
	 */
	List<SysRole> listRoles();

	/**
	 * 获取岗位列表
	 * @return 岗位信息
	 */
	List<SysPost> listPosts();

	/**
	 * 获取人员列表
	 * @return 人员信息
	 */
	List<SysUser> listUsers();

	/**
	 * 获取部门列表
	 * @return 部门信息
	 */
	List<SysDept> listDepts();

}
