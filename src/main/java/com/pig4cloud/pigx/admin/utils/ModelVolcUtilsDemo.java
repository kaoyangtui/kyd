package com.pig4cloud.pigx.admin.utils;

import com.volcengine.ark.runtime.service.ArkService;
import java.time.Duration;
import java.util.*;

public class ModelVolcUtilsDemo {

    public static void main(String[] args) {
        // 1) 准备 API Key：优先从环境变量取；没有就用你类里注入/静态的
        String envKey = System.getenv("ARK_API_KEY");
        if (envKey != null && !envKey.isEmpty()) {
            ModelVolcUtils.staticModelApiKey = envKey;
        }
        if (ModelVolcUtils.staticModelApiKey == null || ModelVolcUtils.staticModelApiKey.isEmpty()) {
            System.err.println("未检测到 ARK_API_KEY，也未配置 staticModelApiKey，请先配置后再运行。");
            return;
        }

        // 2) 替换为你的“批量推理接入点 ID”（ep-bi-...）
        final String MODEL_ID = "ep-bi-20251002112246-8lclx";

        // 3) 准备测试 prompts（示例三条）
        List<String> prompts = Arrays.asList(
                "给我用两句话介绍一下‘批量 Chat’与‘在线 Chat’的区别。",
                "把这句话翻译成英文：我爱吃苹果，也爱喝咖啡。",
                "列出三条 Java 并发下常见的坑，并各用一句话说明原因。"
        );

        // 4) 可选的 system 提示词
        String systemPrompt = "你是严谨的技术助手，回答请简明扼要。";

        // 5) 并发、超时配置（按机器与网络自行调整）
        int maxConcurrency = 16;
        Duration timeout = Duration.ofMinutes(20);

        // 6) 调用批量接口（同步拿结果）
        Map<String, Object> batchResult = ModelVolcUtils.modelCallBatch(
                prompts,
                MODEL_ID,
                maxConcurrency,
                timeout,
                systemPrompt
        );

        // 7) 打印汇总
        System.out.println("\n====== Batch Chat 执行结果汇总 ======");
        System.out.println("success_count = " + batchResult.get("success_count"));
        System.out.println("fail_count    = " + batchResult.get("fail_count"));

        // 8) 打印成功结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> results = (List<Map<String, Object>>) batchResult.getOrDefault("results", Collections.emptyList());
        if (!results.isEmpty()) {
            System.out.println("\n--- 成功结果（按 index 排序） ---");
            for (Map<String, Object> r : results) {
                Integer index = (Integer) r.getOrDefault("index", -1);
                String prompt = Objects.toString(r.get("prompt"), "");
                String id = Objects.toString(r.get("id"), "");
                String text = Objects.toString(r.get("result"), "");
                Object usage = r.get("usage");  // 一般包含 prompt_tokens / completion_tokens / total_tokens

                // 为了控制台整洁，只截断显示前 120 字
                String preview = text.length() > 120 ? text.substring(0, 120) + "..." : text;

                System.out.println("\n[index=" + index + "]");
                System.out.println("prompt: " + prompt);
                System.out.println("id    : " + id);
                System.out.println("result: " + preview);
                System.out.println("usage : " + (usage == null ? "null" : usage.toString()));
            }
        }

        // 9) 打印失败详情（若有）
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> failures = (List<Map<String, Object>>) batchResult.getOrDefault("failures", Collections.emptyList());
        if (!failures.isEmpty()) {
            System.out.println("\n--- 失败详情 ---");
            for (Map<String, Object> f : failures) {
                Integer index = (Integer) f.getOrDefault("index", -1);
                String prompt = Objects.toString(f.get("prompt"), "");
                String error = Objects.toString(f.get("error"), "");
                System.out.println("\n[index=" + index + "]");
                System.out.println("prompt: " + prompt);
                System.out.println("error : " + error);
            }
        }

        System.out.println("\n====== Demo 完成 ======\n");
    }
}
