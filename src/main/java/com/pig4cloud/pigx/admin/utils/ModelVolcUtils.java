package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.yulichang.toolkit.StrUtils;
import com.pig4cloud.pigx.admin.dto.match.DemandMatchDTO;
import com.pig4cloud.pigx.admin.dto.match.PatentMatchDTO;
import com.pig4cloud.pigx.admin.prompt.MatchPrompt;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaoliang
 */
@Slf4j
@Component
public class ModelVolcUtils {

    @Value("${model.huoshan.modelApiKey}")
    private String modelApiKey;
    public static String staticModelApiKey="0ef4713a-70c1-4d45-9226-f6dade8d9026";

    // 在对象初始化后，将实例字段赋值给静态字段
    @PostConstruct
    public void init() {
        staticModelApiKey = modelApiKey;
    }

    /**
     * 批量提交到 Ark Batch Chat，并“同步拿结果”。
     * <p>
     * 关键点：
     * 1) Batch Chat 只支持非流式：必须 stream(false)
     * 2) model 必须是“批量推理接入点 ID”（形如 ep-bi-xxxx），不是在线推理的 ep-xxxx
     * 3) 为 Batch Chat 单独初始化 ArkService（连接池/dispatcher 单独配置）
     * 4) 每条请求的返回体就是一个 ChatCompletion，同步解析 choices[0].message.content
     * <p>
     * 返回结构：
     * - success_count / fail_count
     * - results: List<Map>，每项含 index/prompt/id/result/usage/model
     * - failures: List<Map>，每项含 index/prompt/error
     */
    public static Map<String, Object> modelCallBatch(
            List<String> prompts,                 // 需要批量提交的用户内容
            String model,                         // 模型名称：必须是批量接入点 ID，如 "ep-bi-xxxx"
            int maxConcurrency,                   // 最大并发，建议 <= 5000，按机器能力设置
            Duration timeout,                     // 总体超时，建议 >= 10 分钟
            String systemPrompt                   // 可选的 system 提示词，传 null/空则不加
    ) {
        if (prompts == null || prompts.isEmpty()) {
            log.info("[ModelBatch] empty prompts");
            Map<String, Object> empty = new HashMap<>();
            empty.put("success_count", 0);
            empty.put("fail_count", 0);
            empty.put("results", Collections.emptyList());
            empty.put("failures", Collections.emptyList());
            return empty;
        }
        // 默认参数兜底
        if (timeout == null) {
            timeout = Duration.ofHours(1); // 批量场景建议拉长
        }
        if (maxConcurrency <= 0) {
            maxConcurrency = Math.min(prompts.size(), 512); // 稳妥默认并发
        }

        long startAt = System.currentTimeMillis();
        log.info("[ModelBatch] start: size={}, model={}, concurrency={}, timeout={}s",
                prompts.size(), model, maxConcurrency, timeout.getSeconds());

        // 1) 为 Batch Chat 单独实例化 ArkService（避免与在线流式互相影响）
        ConnectionPool connectionPool = new ConnectionPool(maxConcurrency, 10, TimeUnit.MINUTES);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxConcurrency);
        dispatcher.setMaxRequestsPerHost(maxConcurrency);

        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .timeout(timeout)
                .connectionPool(connectionPool)
                .apiKey(staticModelApiKey) // 使用 @PostConstruct 注入的静态 key
                .build();

        // 2) 并发执行
        ExecutorService executor = Executors.newFixedThreadPool(maxConcurrency);
        CountDownLatch latch = new CountDownLatch(prompts.size());

        AtomicLong success = new AtomicLong(0);
        AtomicLong failure = new AtomicLong(0);

        // 结果与失败列表：并发安全
        List<Map<String, Object>> results = Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> failures = Collections.synchronizedList(new ArrayList<>());
        boolean[] done = new boolean[prompts.size()];

        for (int i = 0; i < prompts.size(); i++) {
            final int idx = i;
            final String prompt = prompts.get(i);

            executor.submit(() -> {
                try {
                    // 3) 组装 messages（可选的 system prompt）
                    final List<ChatMessage> messages = new ArrayList<>();
                    if (StrUtils.isNotBlank(systemPrompt)) {
                        messages.add(ChatMessage.builder()
                                .role(ChatMessageRole.SYSTEM)
                                .content(systemPrompt)
                                .build());
                    }
                    messages.add(ChatMessage.builder()
                            .role(ChatMessageRole.USER)
                            .content(prompt)
                            .build());

                    // 4) 构造 Batch Chat 请求：必须非流式
                    ChatCompletionRequest req = ChatCompletionRequest.builder()
                            .model(model)      // 注意：需要 ep-bi-... 批量接入点 ID
                            .messages(messages)
                            .stream(false)     // 关键：Batch Chat 不支持流式
                            .build();

                    // 5) 同步拿返回体（即 ChatCompletion 对象）
                    var completion = service.createBatchChatCompletion(req);

                    // 6) 解析返回
                    String text = "";
                    if (completion != null
                            && completion.getChoices() != null
                            && !completion.getChoices().isEmpty()
                            && completion.getChoices().get(0).getMessage() != null) {
                        text = completion.getChoices().get(0).getMessage().stringContent();
                    }

                    Map<String, Object> one = new HashMap<>();
                    one.put("index", idx);
                    one.put("prompt", prompt);
                    one.put("id", completion != null ? completion.getId() : null);
                    one.put("result", text);
                    one.put("usage", completion != null ? completion.getUsage() : null);
                    one.put("model", completion != null ? completion.getModel() : null);
                    results.add(one);

                    success.incrementAndGet();
                } catch (Exception e) {
                    failure.incrementAndGet();
                    Map<String, Object> fail = new HashMap<>();
                    fail.put("index", idx);
                    fail.put("prompt", prompt);
                    fail.put("error", e.getMessage());
                    failures.add(fail);
                } finally {
                    done[idx] = true;
                    latch.countDown();
                }
            });
        }

        // 7) 等待全部完成，关闭资源
        try {
            boolean finished = latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!finished) {
                // 标记超时未完成的任务
                int timeoutCount = 0;
                for (int i = 0; i < done.length; i++) {
                    if (!done[i]) {
                        timeoutCount++;
                        Map<String, Object> fail = new HashMap<>();
                        fail.put("index", i);
                        fail.put("prompt", prompts.get(i));
                        fail.put("error", "timeout");
                        failures.add(fail);
                    }
                }
                log.warn("[ModelBatch] timeout, unfinished={}", timeoutCount);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdownNow();
            service.shutdownExecutor();
        }

        // 8) 结果按原输入顺序排序（index 升序）
        results.sort(Comparator.comparingInt(m -> ((Number) m.getOrDefault("index", 0)).intValue()));
        failures.sort(Comparator.comparingInt(m -> ((Number) m.getOrDefault("index", 0)).intValue()));

        // 9) 聚合返回
        Map<String, Object> res = new HashMap<>();
        res.put("success_count", success.get());
        res.put("fail_count", failure.get());
        res.put("results", results);
        res.put("failures", failures);

        long costMs = System.currentTimeMillis() - startAt;
        log.info("[ModelBatch] done: success={}, failure={}, costMs={}", success.get(), failure.get(), costMs);
        return res;
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



    public static void main(String[] args) {
        PatentMatchDTO patentInfoMatchDTO = new PatentMatchDTO();
        patentInfoMatchDTO.setTitle("表面具有连续片层状微纳米结构的聚己内酯丝素蛋白电纺纤维膜及其制备方法和应用");
        patentInfoMatchDTO.setAbs("本发明公开了一种表面具有连续片层状微纳米结构的聚己内酯丝素蛋白电纺纤维膜及其制备方法和应用。所述制备方法包括：步骤(1)将丝素蛋白与聚己内酯溶于六氟异丙醇中，在室温下搅拌溶解，制成混和液；步骤(2)将混合液进行静电纺丝，制备聚己内酯丝素蛋白混纺膜；步骤(3)聚己内酯丝素蛋白混纺膜真空干燥后，与PCL单体、辛酸亚锡催化剂在氮气保护下进行加热反应，制备得到聚己内酯丝素蛋白电纺纤维膜。所述制备方法得到的聚己内酯丝素蛋白电纺纤维膜具有表层片状微纳米结构纤维形貌，比表面积和纤维直径大大增加，增加电纺膜由疏水性向亲水性转变的可能性，大大提高了纤维比表面积，空间维持能力、动态力学性能和降解性能大幅度提高。");
        patentInfoMatchDTO.setNec("棉织造加工;棉印染精加工;毛织造加工;毛染整精加工;麻染整精加工;缫丝加工;绢纺和丝织加工;丝印染精加工;化纤织物染整精加工;针织或钩针编织物印染精加工;纺织带和帘子布制造;服饰制造;皮革鞣制加工;羽毛(绒)加工;化学试剂和助剂制造;专项化学用品制造;纺织专用设备制造;其他仪器仪表制造业;医疗实验室及医用消毒设备和器具制造;其他专用设备制造;服务消费机器人制造;康复辅具制造;医疗、外科及兽医用器械制造");
        patentInfoMatchDTO.setPatentWords("聚己内酯;丝素蛋白;制备;电纺纤维膜;微纳米结构;纤维;辛酸亚锡催化剂;制备方法和应用;动态力学性能;六氟异丙醇;氮气保护;加热反应;降解性能;搅拌溶解;静电纺丝;空间维持;纤维形貌;电纺膜;混和液;混合液;连续片;亲水性;疏水性");
        patentInfoMatchDTO.setTitleKey("制备方法和应用;电纺纤维膜;微纳米结构;聚己内酯;丝素蛋白;连续片");
        patentInfoMatchDTO.setClKey("聚己内酯;丝素蛋白;电纺纤维膜;制备;微纳米结构;连续片;静电纺丝;辛酸亚锡;漂洗;煮沸;碳酸氢钠水溶液;辛酸亚锡催化剂;组织工程支架;六氟异丙醇;氮气保护;电池工业;纺丝电压;加热反应;搅拌溶解;去离子水;天然蚕丝;混和液;混合液;聚合度;摩尔比;再生膜;质量比;纺丝;取出;应用;诱导;重复");
        patentInfoMatchDTO.setBgKey("聚己内酯;丝素蛋白;电纺纤维膜;微纳米结构;连续片;制备;电子扫描显微镜;静电纺丝;纤维形貌;辛酸亚锡;片状结构;电纺膜;断裂力;可吸收;纤维;三维空间结构;改性;机械性能;纤维结构;混合液;扫描图;辛酸亚锡催化剂;制备方法和应用;观察表面;交联改性;产率;蚕丝;规整;降解;漂洗;取出;优选;煮沸;氮气保护环境;动态力学性能;空间网状结构;力学性能检测;分子量检测;六氟异丙醇;表面规则;测试样本;二次手术;纺丝电压;降解能力;降解性能;开环聚合;空间维持;去离子水;扫描电镜;天然蚕丝");

        // Fastjson 序列化示例
        String patentStr = JSON.toJSONString(patentInfoMatchDTO, true);
        DemandMatchDTO dto = new DemandMatchDTO();
        dto.setName("基于纺织品定制化生产智能排产系统研发应用");
        dto.setNec("其他产业用纺织制成品制造;纺织专用设备制造");
        dto.setDescription("1. 提升生产效率和灵活性\n" +
                "APS排产系统能够根据订单需求和生产能力灵活调整生产顺序，优化车间资源配置，减少停机时间，提升生产效率。对于粘扣工厂而言，APS系统可以实现按需生产，提高生产线利用率，减少资源浪费，从而显著提升整体生产效率。此外，APS系统还能够快速响应市场变化，缩短交货周期，提高客户满意度。\n" +
                "2. 降低生产成本和库存积压\n" +
                "通过合理安排生产计划，APS系统可以减少物料浪费和库存积压，降低生产成本。例如，APS系统能够实现物料需求预测到生产计划执行的全流程自动化，优化库存管理，减少库存成本。这对于粘扣工厂来说，能够有效降低生产成本，提高市场竞争力。\n" +
                "3. 提高产品质量和客户满意度\n" +
                "APS系统通过实时监控生产过程，及时解决瓶颈问题，确保产品质量。例如，在制鞋行业，APS系统显著提升了生产效率和经济效益，解决了长期困扰的排产问题。对于粘扣工厂而言，APS系统同样能够提高产品质量，减少生产事故质量，提升客户满意度。\n" +
                "4. 推动产业链供应链自主创新能力\n" +
                "APS系统作为供应链管理的核心工具，能够实现企业内外部全数据链的整合，提供全局链思维管理。通过APS系统的应用，粘扣工厂可以实现供应链上下游的协同优化，提升供应链的整体效率和响应能力。这不仅有助于增强产业链供应链的自主创新能力，还能推动我市产业转型升级。\n" +
                "5. 促进数字化转型和智能制造\n" +
                "APS系统的应用是智能制造转型的重要组成部分。通过APS系统，粘扣工厂可以实现生产计划的精细化管理，提高生产效率和产品质量。例如，APS系统与ERP系统的深度融合，可以实现生产计划与实际生产的无缝衔接，提高生产透明度和管理效率。这对于推动我市制造业的数字化转型具有重要意义。\n" +
                "6. 应对市场需求变化和订单波动\n" +
                "粘扣工厂面临的需求订单不稳定、工艺变化和设备故障等复杂约束条件。APS系统能够快速适应这些变化，优化生产计划，确保生产顺利进行。例如，在离散型生产中，APS系统能够有效应对订单需求的波动，避免产能过剩或不足。\n" +
                "7. 提高企业竞争力和市场响应速度\n" +
                "APS系统的应用能够提高企业的市场响应速度和竞争力。通过优化生产计划和资源分配，APS系统能够帮助企业快速适应市场需求变化，提高市场竞争力。例如，在汽车零部件行业，APS系统显著提高了生产效率和库存管理能力。\n" +
                "8.打造企业级数字化一体平台\n" +
                "解决系统建设及管理分散，缺乏集成整合，数据无法打通，造成信息孤岛等纺织企业经营痛点，整合ERP、MES、APS等系统，通过物联网、及大数据技术，打通生产、仓储、供应链全链路。聚焦纺织行业特性，支持智能排产、质量追溯、能耗优化及设备实时监控，实现订单敏捷响应与工艺动态调整");
        dto.setTags("纺织");

        // Fastjson 序列化示例
        String demandStr = com.alibaba.fastjson.JSON.toJSONString(dto, true);
        String content = StrUtil.format(MatchPrompt.VALUE, patentStr, demandStr);
        Map<String, Object> modelResult = modelCall(content,"ep-20250731154231-nn5qb");
        System.out.println(modelResult);
    }
}
