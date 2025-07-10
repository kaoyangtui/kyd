package com.pig4cloud.pigx.admin.mq;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.constants.FileGroupTypeEnum;
import com.pig4cloud.pigx.admin.constants.PatentFileTypeEnum;
import com.pig4cloud.pigx.admin.constants.TopicConstants;
import com.pig4cloud.pigx.admin.entity.PatentDetailCacheEntity;
import com.pig4cloud.pigx.admin.entity.PatentDetailEntity;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = TopicConstants.TOPIC_PATENT,
        consumerGroup = TopicConstants.CONSUMER_GROUP_PATENT + "_" + TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}",
        selectorExpression = TopicConstants.TOPIC_PATENT_TAG + "_" + "${spring.profiles.active}"
)
public class PatentConsumer implements RocketMQListener<String> {

    private final PatentInfoService patentInfoService;
    private final PatentDetailService patentDetailService;
    private final PatentInventorService patentInventorService;
    private final PatentMonitorService patentMonitorService;
    private final PatentDetailCacheService patentDetailCacheService;
    private final YtService ytService;
    private final FileService fileService;

    @Override
    public void onMessage(String message) {
        try {
            if (StrUtil.isBlank(message)) {
                log.warn("接收到的消息为空，跳过处理");
                return;
            }

            log.info("========== 【专利消息处理开始】==========");
            log.debug("接收到消息内容: {}", message);

            // 主表信息保存
            PatentInfoEntity patentInfo = JSONUtil.toBean(message, PatentInfoEntity.class);
            patentInfo.setTenantId(1L);
            log.info("解析主表信息完成，专利ID: {}", patentInfo.getPid());
            patentInfoService.create(patentInfo, message);
            log.info("主表信息保存完成");

            //详情信息保存
            PatentDetailEntity oldDetail = patentDetailService.lambdaQuery()
                    .eq(PatentDetailEntity::getPid, patentInfo.getPid())
                    .one();
            PatentDetailEntity newDetail = JSONUtil.toBean(message, PatentDetailEntity.class);
            newDetail.setCitationForwardCountry(CodeUtils.formatCodes(newDetail.getCitationForwardCountry()));

            if (ObjectUtil.isEmpty(oldDetail)) {
                newDetail.setId(null);
                newDetail.setTenantId(1L);
                patentDetailService.save(newDetail);
            } else {
                newDetail.setId(oldDetail.getId());
                // 如果只想部分字段更新，建议用 BeanUtil.copyProperties(newDetail, oldDetail, CopyOptions.create().ignoreNullValue());
                patentDetailService.updateById(newDetail);
            }

            //处理详情缓存中的主图
            PatentDetailCacheEntity detailCache = patentDetailCacheService.lambdaQuery()
                    .eq(PatentDetailCacheEntity::getPid, patentInfo.getPid()).one();
            if (ObjectUtil.isEmpty(detailCache)) {
                detailCache = new PatentDetailCacheEntity();
                detailCache.setPid(patentInfo.getPid());
                String absUrl = ytService.absUrl(patentInfo.getPid());
                if (StrUtil.isNotBlank(absUrl)) {
                    detailCache.setDraws(fileService.uploadFileByUrl(absUrl, PatentFileTypeEnum.ABSTRACT.getCode(), FileGroupTypeEnum.IMAGE));
                }
                detailCache.setStatus(0);
                detailCache.setTenantId(1L);
                patentDetailCacheService.save(detailCache);
            }

            // 发明人处理
            List<String> inventorNames = StrUtil.split(patentInfo.getInventorName(), ';');
            log.info("发明人分割完成，共 {} 人", inventorNames.size());
            patentInventorService.create(patentInfo.getPid(), inventorNames);
            log.info("发明人信息保存完成");

            // 专利监控
            patentMonitorService.create(message);
            log.info("专利监控信息保存完成");

            log.info("========== 【专利消息处理结束】==========");

        } catch (Exception e) {
            log.error("========== 【专利消息处理异常】==========");
            log.error("消息内容: {}", message);
            log.error("异常信息: {}", e.getMessage(), e);
        }
    }

}
