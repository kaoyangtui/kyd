package com.pig4cloud.pigx.admin.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationOutput;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaoliang
 */
@Slf4j
public class ModelAliUtils {
    private static final String MODEL_API_KEY = "sk-8dc3000e26784439868f4174117fe3c8";

    @NotNull
    public static Map<String, Object> modelCall(String content, String model) throws Exception {
        // 设置环境变量
        System.setProperty("DASHSCOPE_CONNECTION_TIMEOUT", "600000");  // 60秒
        System.setProperty("DASHSCOPE_READ_TIMEOUT", "600000");        // 60秒

        Generation gen = new Generation();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(content)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(MODEL_API_KEY)
                .model(model)
                .messages(Collections.singletonList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                // 开启流式输出
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> result;
        try {
            result = gen.streamCall(param);
        } catch (Exception e) {
            log.error("modelCall error:{},content:{}", e.getMessage(), content, e);
            throw new Exception("modelCall error");
        }
        // 初始化 StringBuilder 用于存储最终回答
        StringBuilder fullAnswerBuilder = new StringBuilder();
        StringBuilder reasoningBuilder = new StringBuilder();
        // 用于累加 token 数量（线程安全）
        AtomicInteger totalInputTokens = new AtomicInteger(0);
        AtomicInteger totalOutputTokens = new AtomicInteger(0);
        // 使用 blockingSubscribe 阻塞等待流处理完成
        result.blockingSubscribe(
                generationResult -> {
                    // 如果返回结果中包含 token 使用信息，则累加到总计中
                    if (generationResult.getUsage() != null) {
                        int currentInputTokens = generationResult.getUsage().getInputTokens();
                        int currentOutputTokens = generationResult.getUsage().getOutputTokens();
                        totalInputTokens.set(currentInputTokens);
                        totalOutputTokens.set(currentOutputTokens);
                    }

                    // 获取推理思维过程（可选）
                    String reasoning = generationResult.getOutput().getChoices().get(0).getMessage().getReasoningContent();
                    if (reasoning != null && !reasoning.isEmpty()) {
                        reasoningBuilder.append(reasoning);
                    }

                    // 获取最终回答
                    String outputContent = generationResult.getOutput().getChoices().get(0).getMessage().getContent();
                    if (outputContent != null && !outputContent.isEmpty()) {
                        fullAnswerBuilder.append(outputContent);
                    }
                },
                error -> {
                    throw new RuntimeException("流处理发生异常", error);
                }
        );
        Map<String, Object> resMap = Maps.newConcurrentMap();
        resMap.put("result", fullAnswerBuilder.toString());
        resMap.put("reasoning", reasoningBuilder.toString());
        resMap.put("total_input_tokens", totalInputTokens.get());
        resMap.put("total_output_tokens", totalOutputTokens.get());
        return resMap;
    }

    @NotNull
    public static String modelCallWithImg(String content) throws Exception {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("image", "https://speeds.oss-cn-shanghai.aliyuncs.com/huang/extreme/20250324/37531-20250324110129047.mp4?x-oss-process=video/snapshot,t_0,f_jpg"),
                        Collections.singletonMap("text", content))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(MODEL_API_KEY)
                // 此处以qwen-vl-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-vl-max-2025-01-25")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);

        log.info("result: {}", JsonUtils.toJson(result));
        if (result == null) {
            // 处理 result 为 null 的情况
            throw new Exception("Result is null");
        }

        MultiModalConversationOutput output = result.getOutput();
        if (output == null) {
            throw new Exception("Output is null");
        }

        List<MultiModalConversationOutput.Choice> choices = output.getChoices();
        if (choices == null || choices.isEmpty()) {
            // 处理 choices 为 null 或者为空的情况
            throw new Exception("Choices is null or empty");
        }

        MultiModalConversationOutput.Choice firstChoice = choices.get(0);
        if (firstChoice == null) {
            // 处理 firstChoice 为 null 的情况
            throw new Exception("First choice is null");
        }

        MultiModalMessage message = firstChoice.getMessage();
        if (message == null) {
            // 处理 message 为 null 的情况
            throw new Exception("Message is null");
        }

        content = message.getContent().get(0).getOrDefault("text", "").toString();

        if (content == null) {
            // 处理 content 为 null 的情况
            throw new Exception("Content is null");
        }
        return content;
    }

}
