package com.pig4cloud.pigx.admin.service;

import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pigx.admin.constants.MessageTemplateEnum;

import java.util.List;

public interface MessageService {

    SysMessageEntity buildMessage(MessageTemplateEnum templateEnum, Object... params);

    void saveMessage(List<SysMessageEntity> sysMessageList);

    void saveMessageRelation(List<SysMessageRelationEntity> sysMessageRelationList);
}
