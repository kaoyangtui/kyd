package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pigx.admin.constants.MessageTemplateEnum;
import com.pig4cloud.pigx.admin.constants.SysMessageCategoryEnum;
import com.pig4cloud.pigx.admin.mapper.SysMessageRelationMapper;
import com.pig4cloud.pigx.admin.service.MessageService;
import com.pig4cloud.pigx.admin.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final SysMessageService sysMessageService;
    private final SysMessageRelationMapper sysMessageRelationMapper;

    @Override
    public SysMessageEntity buildMessage(MessageTemplateEnum templateEnum, Object... params) {
        long id = IdUtil.getSnowflakeNextId();
        SysMessageEntity msg = new SysMessageEntity();
        msg.setId(id);
        msg.setCategory(SysMessageCategoryEnum.LETTER.getCode());
        msg.setTitle(templateEnum.getDescription());
        msg.setContent(StrUtil.format(templateEnum.getTemplate(), params));
        msg.setSendFlag("1");
        msg.setAllFlag("0");
        msg.setSort((int) (System.currentTimeMillis() / 1000));
        return msg;
    }

    @Override
    public void saveMessage(List<SysMessageEntity> sysMessageList) {
        sysMessageService.saveBatch(sysMessageList);
    }

    @Override
    public void saveMessageRelation(List<SysMessageRelationEntity> sysMessageRelationList) {
        sysMessageRelationMapper.insert(sysMessageRelationList);
    }
}
