package com.pig4cloud.pigx.utils;

import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoliang
 */
public class RocketMQUtils {
    public static String getMessageBody(ByteBuffer buffer) {
        // 避免改变原始缓冲区状态
        ByteBuffer duplicate = buffer.duplicate();
        byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void sendMessage(RocketMQClientTemplate rocketMQClientTemplate, String topic, String body) {
        // 处理数字人训练
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void beforeCommit(boolean readOnly) {
                // 不做操作
            }

            @Override
            public void afterCommit() {
                rocketMQClientTemplate.syncSendNormalMessage(
                        topic,
                        body);
            }

            @Override
            public void afterCompletion(int status) {
                // 处理事务完成后的清理操作（如果需要）
            }
        });
    }
}
