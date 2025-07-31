package com.pig4cloud.pigx.admin.utils;

import com.github.yulichang.toolkit.StrUtils;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaoliang
 */
@Component
public class ModelVolcUtils {

    @Value("${model.huoshan.modelApiKey}")
    private String modelApiKey;
    public static String staticModelApiKey;

    // 在对象初始化后，将实例字段赋值给静态字段
    @PostConstruct
    public void init() {
        staticModelApiKey = modelApiKey;
    }

    public static Map<String, Object> modelCall(String content, String model) {
        ArkService service = ArkService.builder().apiKey(staticModelApiKey).build();

        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamUserMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content(content)
                .build();
        streamMessages.add(streamUserMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .stream(true)
                .streamOptions(new ChatCompletionRequest.ChatCompletionRequestStreamOptions(true, true))
                .messages(streamMessages)
                .build();

        StringBuilder fullAnswerBuilder = new StringBuilder();
        StringBuilder reasoningBuilder = new StringBuilder();
        AtomicLong totalInputTokens = new AtomicLong(0);
        AtomicLong totalOutputTokens = new AtomicLong(0);

        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(choice -> {
                            if (choice.getUsage() != null) {
                                long currentInputTokens = choice.getUsage().getPromptTokens();
                                long currentOutputTokens = choice.getUsage().getCompletionTokens();
                                totalInputTokens.set(currentInputTokens);
                                totalOutputTokens.set(currentOutputTokens);
                            }
                            if (!choice.getChoices().isEmpty()) {
                                String reasoning = choice.getChoices().get(0).getMessage().getReasoningContent();
                                if (StrUtils.isNotBlank(reasoning)) {
                                    reasoningBuilder.append(reasoning);
                                }

                                String outputContent = choice.getChoices().get(0).getMessage().stringContent();
                                if (StrUtils.isNotBlank(outputContent)) {
                                    fullAnswerBuilder.append(outputContent);
                                }
                            }
                        }
                );

        service.shutdownExecutor();

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("result", fullAnswerBuilder.toString());
        resMap.put("reasoning", reasoningBuilder.toString());
        resMap.put("total_input_tokens", totalInputTokens.get());
        resMap.put("total_output_tokens", totalOutputTokens.get());
        return resMap;
    }

    public static Map<String, Object> modelCallWithImg(String content, List<String> imageUrls, String model) {
        ArkService service = ArkService.builder().apiKey(staticModelApiKey).build();

        final List<ChatMessage> messages = new ArrayList<>();
        final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            multiParts.add(ChatCompletionContentPart.builder()
                    .type("image_url")
                    .imageUrl(new ChatCompletionContentPart.ChatCompletionContentPartImageURL(imageUrl))
                    .build()
            );
        }

        multiParts.add(ChatCompletionContentPart.builder()
                .type("text")
                .text(content)
                .build()
        );

        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER)
                .multiContent(multiParts).build();
        messages.add(userMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .stream(true)
                .streamOptions(new ChatCompletionRequest.ChatCompletionRequestStreamOptions(true, true))
                .messages(messages)
                .build();

        StringBuilder fullAnswerBuilder = new StringBuilder();
        StringBuilder reasoningBuilder = new StringBuilder();
        AtomicLong totalInputTokens = new AtomicLong(0);
        AtomicLong totalOutputTokens = new AtomicLong(0);

        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(choice -> {
                            if (choice.getUsage() != null) {
                                long currentInputTokens = choice.getUsage().getPromptTokens();
                                long currentOutputTokens = choice.getUsage().getCompletionTokens();
                                totalInputTokens.set(currentInputTokens);
                                totalOutputTokens.set(currentOutputTokens);
                            }
                            if (!choice.getChoices().isEmpty()) {
                                String reasoning = choice.getChoices().get(0).getMessage().getReasoningContent();
                                if (StrUtils.isNotBlank(reasoning)) {
                                    reasoningBuilder.append(reasoning);
                                }

                                String outputContent = choice.getChoices().get(0).getMessage().stringContent();
                                if (StrUtils.isNotBlank(outputContent)) {
                                    fullAnswerBuilder.append(outputContent);
                                }
                            }
                        }
                );

        service.shutdownExecutor();

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("result", fullAnswerBuilder.toString());
        resMap.put("reasoning", reasoningBuilder.toString());
        resMap.put("total_input_tokens", totalInputTokens.get());
        resMap.put("total_output_tokens", totalOutputTokens.get());
        return resMap;
    }
}