package com.pig4cloud.pigx.jsonflow.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.common.websocket.distribute.MessageDO;
import com.pig4cloud.pigx.common.websocket.distribute.RedisMessageDistributor;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import com.pig4cloud.pigx.jsonflow.api.constant.SimpMsgConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.WsNotice;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.mapper.WsNoticeMapper;
import com.pig4cloud.pigx.jsonflow.message.SendMessage;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.support.WsNoticeBuilder;
import lombok.experimental.UtilityClass;

import jakarta.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * WS和钉钉发送消息工具类封装
 *
 * @author luolin
 * @date 2020/2/5 22:31
 */
@UtilityClass
public class SimpMsgTempUtils {

	private final WsNoticeMapper wsNoticeMapper = SpringContextHolder.getBean(WsNoticeMapper.class);

	private final RedisMessageDistributor messageDistributor = SpringContextHolder.getBean(RedisMessageDistributor.class);

	private final RunFlowService runFlowService = SpringContextHolder.getBean(RunFlowService.class);

	/**
	 * 发送消息给个人
	 * @param nodeName 任务名称
	 */
	public void convertAndSendToUser(String nodeName, Long userId) {
		SendMessage<String> sm = SendMessage.sendToUser(Collections.singleton(userId), nodeName);
		sendWebsocketMsg(sm);
	}

	/**
	 * 当为结束节点时，通知任务领导、发起人
	 * @param runJobVO    运行任务
	 * @param remindUsers 通知人
	 */
	public void nodeEndNotifyUser(RunJobVO runJobVO, Set<Long> remindUsers) {
		if (CollUtil.isEmpty(remindUsers))
			return;
		String name = getFlowNameByKey(runJobVO.getFlowInstId());
		SendMessage<String> sm = SendMessage.sendToUser(remindUsers, name, FlowStatusEnum.FINISH.getStatus());
		sendWebsocketMsg(sm);
		// 钉钉通知
		nodeEndNotifyDdUser(runJobVO.getFlowInstId(), runJobVO.getFlowKey(), name, remindUsers);
	}

	/**
	 * 当为结束节点时，通过钉钉通知任务领导、发起人
	 * @param flowKey 业务KEY
	 * @param remindUsers 通知人
	 */
	private void nodeEndNotifyDdUser(Long flowInstId, String flowKey, String name, Set<Long> remindUsers) {
		String data = SimpMsgConstants.OWN_NODE + name + SimpMsgConstants.END_NODE;
		// 保存
		List<WsNotice> wsNotices = WsNoticeBuilder.defUserWsNotice3(remindUsers, data, flowInstId, flowKey);
		wsNotices.forEach(wsNoticeMapper::insert);
		// wsNoticeMapper.insertBatchSomeColumn(wsNotices);
		// 发送钉钉
		// MessageHelper.sendWorkMessage(CollUtil.join(remindUsers, StrUtil.COMMA), data,
		// DingTalkConstant.TEXT);
	}

	private void doNotifyDdMsg(List<SysUser> ddUsers, String msgText, List<WsNotice> wsNotices) {
		wsNotices.forEach(wsNoticeMapper::insert);
		// wsNoticeMapper.insertBatchSomeColumn(wsNotices);

		// 发送钉钉
		// MessageHelper.sendWorkMessage(CollUtil.join(remindDdUserList, StrUtil.COMMA),
		// msgText, DingTalkConstant.TEXT);
	}

	/**
	 * 流程中任务通知办理人
	 * @param ddUsers 钉钉用户
	 * @param runJobVO 任务
	 */
	public void nodeNotifyUsers(RunJobVO runJobVO, List<SysUser> ddUsers, String nodeName, boolean isUser) {
		if (CollUtil.isEmpty(ddUsers))
			return;
		Set<Long> remindUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		SendMessage<String> sm = isUser ? SendMessage.sendToUser(remindUsers, nodeName)
				: SendMessage.sendToRole(remindUsers, nodeName);
		sendWebsocketMsg(sm);
		// 钉钉通知
		nodeNotifyDdUsers(runJobVO, ddUsers, nodeName);
	}

	/**
	 * 流程中任务通过钉钉通知办理人
	 * @param runJobVO 运行任务
	 * @param ddUsers 钉钉用户
	 * @param nodeName 节点名称
	 */
	private void nodeNotifyDdUsers(RunJobVO runJobVO, List<SysUser> ddUsers, String nodeName) {
		// 构建提示语
		String initiatorName = StrUtil.isEmpty(runJobVO.getInitiatorName()) ? StrUtil.EMPTY
				: SimpMsgConstants.INITIATOR_ID + runJobVO.getInitiatorName();
		String name = getFlowNameByKey(runJobVO.getFlowInstId());
		String text = SimpMsgConstants.NEW_NODE + initiatorName + SimpMsgConstants.ORDER_TYPE + name
				+ SimpMsgConstants.HANDLE_NODE + nodeName;
		// 保存
		Set<Long> sysUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		List<WsNotice> wsNotices = WsNoticeBuilder.defUserWsNotice2(sysUsers, text, runJobVO.getFlowInstId(), runJobVO.getFlowKey());
		doNotifyDdMsg(ddUsers, text, wsNotices);
	}

	/**
	 * 转办任务通知办理人
	 * @param nodeName 节点名称
	 * @param sysUsers 通知人
	 * @param ddText 钉钉消息
	 */
	public void turnNodeNotifyUser(String nodeName, List<SysUser> sysUsers, Long flowInstId, String flowKey, String ddText) {
		Set<Long> remindUsers = sysUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		SendMessage<String> sm = SendMessage.sendToUser(remindUsers, nodeName);
		sendWebsocketMsg(sm);
		// 钉钉通知
		turnNodeNotifyDdUser(sysUsers, ddText, flowInstId, flowKey);
	}

	/**
	 * 转办任务通过钉钉通知办理人
	 * @param ddUsers 通知人
	 * @param text 消息
	 */
	private void turnNodeNotifyDdUser(List<SysUser> ddUsers, String text, Long flowInstId, String flowKey) {
		Set<Long> remindUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		// 保存
		List<WsNotice> wsNotices = WsNoticeBuilder.defUserWsNotice2(remindUsers, text, flowInstId, flowKey);
		wsNotices.forEach(wsNoticeMapper::insert);
		// wsNoticeMapper.insertBatchSomeColumn(wsNotices);
		// String ddUserId = sysUser.getDdUserId();
		// 发送钉钉
		// if (StrUtil.isNotBlank(ddUserId)) MessageHelper.sendWorkMessage(ddUserId, text,
		// DingTalkConstant.TEXT);
	}

	/**
	 * 任务抄送通过站内通知
	 * @param ddUsers 钉钉用户
	 * @param text 消息
	 */
	public void nodeCopyNotifyUsers(List<SysUser> ddUsers, String text, Long flowInstId, String flowKey) {
		if (CollUtil.isEmpty(ddUsers))
			return;
		Set<Long> remindUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		SendMessage<String> sm = SendMessage.sendToUser(remindUsers, text);
		sendWebsocketMsg(sm);
		// 钉钉通知
		nodeCopyNotifyDdUsers(ddUsers, text, flowInstId, flowKey);
	}

	/**
	 * 任务抄送通过钉钉通知
	 * @param ddUsers 钉钉用户
	 * @param text 消息
	 */
	public void nodeCopyNotifyDdUsers(List<SysUser> ddUsers, String text, Long flowInstId, String flowKey) {
		// 保存
		Set<Long> sysUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		List<WsNotice> wsNotices = WsNoticeBuilder.defUserWsNotice2(sysUsers, text, flowInstId, flowKey);
		doNotifyDdMsg(ddUsers, text, wsNotices);
	}

	/**
	 * 钉钉通知发起人（排除自己）
	 *
	 * @param ddUsers    钉钉用户
	 * @param initiatorId 发起人
	 */
	public void turnNodeNotifyDdUser(RunFlow runFlow, List<SysUser> ddUsers, Long initiatorId) {
		SysUser sysUser = ddUsers.stream().filter(f -> f.getUserId().equals(initiatorId)).findAny().orElseThrow(() -> new ValidationException("创建人不能为空"));
		SysUser currUser = ddUsers.stream().filter(f -> f.getUserId().equals(SecurityUtils.getUser().getId())).findAny().orElseThrow(() -> new ValidationException("当前用户不能为空"));
		// 构建提示语
		String flowKey = runFlow.getFlowKey();
		String fromName = getFlowNameByKey(runFlow.getId());
		String code = runFlow.getCode();
		String text = "(作废)" + fromName + " 由 " + currUser.getName() + "作废, 请悉知!工单编号: " + code;
		turnNodeNotifyDdUser(CollUtil.newArrayList(sysUser), text, runFlow.getId(), flowKey);
	}

	/**
	 * 返回流程名称
	 * @param flowInstId 流程实例ID
	 */
	public String getFlowNameByKey(Long flowInstId){
		if (Objects.isNull(flowInstId)) {
			throw new ValidationException("流程实例ID不能为空");
		}
		RunFlow runFlow = runFlowService.getById(flowInstId);
		if (Objects.isNull(runFlow)) throw new ValidationException("流程实例不存在");
		return runFlow.getFlowName();
	}

	/**
	 * 发送消息
	 * @param sm 消息
	 */
	public void sendWebsocketMsg(SendMessage<String> sm) {
		// websocket 发送消息
		MessageDO messageDO = new MessageDO();
		messageDO.setNeedBroadcast(Boolean.FALSE);
		// 目标用户ID
		messageDO.setSessionKeys(new ArrayList<>(sm.getUserIds()));
		messageDO.setMessageText(JSONUtil.toJsonStr(sm));
		messageDistributor.distribute(messageDO);
	}

}
