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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // 跳过的参数类型
    private static final Set<Class<?>> SKIPPED_PARAM_TYPES = CollUtil.newHashSet(
            HttpServletRequest.class,
            HttpServletResponse.class,
            MultipartFile.class,
            org.springframework.validation.BindingResult.class
    );

    // JSON 配置：忽略 null、异常、格式时间
    private static final JSONConfig JSON_CONFIG = new JSONConfig()
            .setIgnoreNullValue(true)
            .setIgnoreError(true)
            .setDateFormat("yyyy-MM-dd HH:mm:ss");

    @Around("execution(public * com.pig4cloud.pigx.admin.controller..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();

        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);

        String url = request != null ? request.getRequestURI() : "N/A";
        String httpMethod = request != null ? request.getMethod() : "N/A";
        String paramsJson = processParameters(pjp);

        log.info("Request Start => [{} {}], Params: {}", httpMethod, url, paramsJson);

        try {
            Object response = pjp.proceed();
            watch.stop();

            String responseJson = JSONUtil.toJsonStr(response, JSON_CONFIG);
            responseJson = StrUtil.subPre(responseJson, 1000); // 截断输出

            // 可选：提取统一响应结构 code/message
            String code = "-";
            String message = "-";
            if (response != null && response.getClass().getSimpleName().equals("R")) {
                try {
                    Object codeObj = response.getClass().getMethod("getCode").invoke(response);
                    Object msgObj = response.getClass().getMethod("getMsg").invoke(response);
                    code = String.valueOf(codeObj);
                    message = String.valueOf(msgObj);
                } catch (Exception ignore) {
                }
            }

            log.info("Request Finish => [{} {}], Code: {}, Time: {}ms, Response: {}",
                    httpMethod, url, code, watch.getTotalTimeMillis(), responseJson);

            return response;
        } catch (Throwable e) {
            watch.stop();
            log.error("Request Error => [{} {}], Time: {}ms", httpMethod, url, watch.getTotalTimeMillis(), e);
            throw e;
        }
    }

    /**
     * 处理请求参数（忽略上传/请求等不可序列化对象）
     */
    private String processParameters(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (ArrayUtil.isEmpty(args)) {
            return "{}";
        }

        String[] paramNames = ((MethodSignature) pjp.getSignature()).getParameterNames();

        Map<String, Object> params = IntStream.range(0, args.length)
                .filter(i -> {
                    Object arg = args[i];
                    return arg != null && SKIPPED_PARAM_TYPES.stream()
                            .noneMatch(clazz -> ClassUtil.isAssignable(clazz, arg.getClass()));
                })
                .boxed()
                .collect(Collectors.toMap(
                        i -> paramNames.length > i ? paramNames[i] : "arg" + i,
                        i -> args[i],
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));

        return JSONUtil.toJsonStr(params, JSON_CONFIG);
    }
}
