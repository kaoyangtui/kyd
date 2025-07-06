package com.pig4cloud.pigx.admin.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class ApiControllerAOP {

    // 需要跳过的参数类型（Hutool 5.8.36 兼容写法）
    private static final Set<Class<?>> SKIPPED_PARAM_TYPES = CollUtil.newHashSet(
            HttpServletRequest.class,
            HttpServletResponse.class,
            MultipartFile.class,
            org.springframework.validation.BindingResult.class
    );

    // JSON 序列化配置
    private static final JSONConfig JSON_CONFIG = new JSONConfig()
            .setIgnoreNullValue(true)
            .setIgnoreError(true) // 忽略序列化异常
            .setDateFormat("yyyy-MM-dd HH:mm:ss");

    @Around("execution(public * com.pig4cloud.pigx.admin.controller..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();

        // 获取请求信息（Hutool 安全处理）
        HttpServletRequest request = (HttpServletRequest) Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);

        String url = request != null ? request.getRequestURI() : "N/A";
        String httpMethod = request != null ? request.getMethod() : "N/A";

        // 处理请求参数
        String paramsJson = processParameters(pjp);

        log.info("Request Start => URL: [{}], Method: [{}], Params: {}", url, httpMethod, paramsJson);

        try {
            Object response = pjp.proceed();
            watch.stop();

            // 序列化响应（Hutool 安全处理）
            String responseJson = JSONUtil.toJsonStr(response, JSON_CONFIG);
            responseJson = StrUtil.subPre(responseJson, 1000); // 截断过长的响应

            log.info("Request Finish => URL: [{}], Time: [{}ms], Response: {}",
                    url, watch.getTotalTimeMillis(), responseJson);

            return response;
        } catch (Throwable e) {
            watch.stop();
            log.error("Request Error => URL: [{}], Time: [{}ms], Error: {}",
                    url, watch.getTotalTimeMillis(), e.getMessage());
            throw e;
        }
    }

    /**
     * 处理请求参数（修复索引和类型检查问题）
     */
    private String processParameters(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (ArrayUtil.isEmpty(args)) {
            return "{}";
        }

        String[] paramNames = ((MethodSignature) pjp.getSignature()).getParameterNames();

        // 使用流处理参数（避免索引越界）
        Map<String, Object> params = IntStream.range(0, args.length)
                .filter(i -> {
                    Object arg = args[i];
                    return SKIPPED_PARAM_TYPES.stream()
                            .noneMatch(clazz -> ClassUtil.isAssignable(clazz, arg.getClass()));
                })
                .boxed()
                .collect(Collectors.toMap(
                        i -> paramNames.length > i ? paramNames[i] : "arg" + i, // 处理参数名缺失
                        i -> args[i],
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new // 保持顺序
                ));

        // Hutool 安全序列化
        return JSONUtil.toJsonStr(params, JSON_CONFIG);
    }
}