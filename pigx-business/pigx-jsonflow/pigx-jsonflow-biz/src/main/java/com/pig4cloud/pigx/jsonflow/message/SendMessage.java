package com.pig4cloud.pigx.jsonflow.message;

import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.MsgTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * 服务端发送消息结构
 *
 * @author luolin
 * @date 2020/2/4 12:00
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "发送消息对象")
public class SendMessage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Set<Long> userIds;

	/**
	 * et 租户ID
	 */
	private Long tenantId;

	/**
	 * 消息体
	 */
	private T data;

	/**
	 * 消息类型 TODO 0-个人消息 1-群消息
	 */
	private String type;

	/**
	 * 任务类型（ 0-个人任务 1-组任务）
	 */
	@Schema(description = "任务类型")
	private String jobType;

	/**
	 * 通知状态 -1发起 0流程中 1结束 流程状态（0：运行中（正常） 1：完结 2：流程作废）
	 */
	@Schema(description = "任务状态")
	private String status;

	/**
	 * 默认个人任务未完成
	 * @param userIds 用户iD
	 * @param data 数据
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToUser(Set<Long> userIds, T data) {
		return sendToUser(userIds, data, FlowStatusEnum.RUN.getStatus());
	}

	/**
	 * 默认组-个人任务消息未完成
	 * @param userIds 用户iD
	 * @param data 数据
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToRole(Set<Long> userIds, T data) {
		return sendToRole(userIds, data, FlowStatusEnum.RUN.getStatus());
	}

	/**
	 * 默认个人任务消息
	 * @param userIds 用户iD
	 * @param data 数据
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToUser(Set<Long> userIds, T data, String status) {
		return sendToUser(userIds, data, MsgTypeEnum.PERSONAL.getType(), JobUserTypeEnum.USER.getType(), status);
	}

	/**
	 * 默认组-个人任务消息
	 * @param userIds 用户iD
	 * @param data 数据
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToRole(Set<Long> userIds, T data, String status) {
		return sendToUser(userIds, data, MsgTypeEnum.GROUP.getType(), JobUserTypeEnum.ROLE.getType(), status);
	}

	/**
	 * 默认自己-个人任务消息（未完成）
	 * @param data 数据
	 * @param type 类型
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToSelf(T data, String type) {
		return sendToSelf(data, type, JobUserTypeEnum.USER.getType(), FlowStatusEnum.RUN.getStatus());
	}

	/**
	 * 自动获取当前用户userId
	 * @param data 数据
	 * @param type 类型
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToSelf(T data, String type, String jobType, String status) {
		Set<Long> userIds = Collections.singleton(SecurityUtils.getUser().getId());
		return sendToUser(userIds, data, type, jobType, status);
	}

	/**
	 * 上下文自动获取tenantId
	 * @param userIds 用户iD
	 * @param data 数据
	 * @param type 类型
	 * @param <T> 数据类型
	 */
	public static <T> SendMessage<T> sendToUser(Set<Long> userIds, T data, String type, String jobType, String status) {
		Long tenantId = TenantContextHolder.getTenantId();
		return buildMsg(userIds, tenantId, data, type, jobType, status);
	}

	/**
	 * 消息体封装
	 * @param userIds 用户iD
	 * @param tenantId 租户ID
	 * @param data 数据
	 * @param type 类型
	 * @param <T> 数据类型
	 */
	private static <T> SendMessage<T> buildMsg(Set<Long> userIds, Long tenantId, T data, String type, String jobType,
			String status) {
		SendMessage<T> msgRe = new SendMessage<>();
		msgRe.setUserIds(userIds).setTenantId(tenantId).setData(data).setType(type);
		msgRe.setJobType(jobType).setStatus(status);
		return msgRe;
	}

}
