/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pigx;

import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnableOpenApi;
import jakarta.annotation.PostConstruct;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * @author lengleng 单体版本启动器，只需要运行此模块则整个系统启动
 */
@EnableOpenApi(value = "admin", isMicro = false)
@EnablePigxResourceServer
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement  // 启用事务管理
@Import(RocketMQAutoConfiguration.class)
public class App {


    private final ApplicationContext context;

    public App(ApplicationContext context) {
        this.context = context;
    }
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

//    @PostConstruct
//    public void checkJsonFlowController() {
//        // 1. Bean 基本信息
//        Object bean = context.getBean("jsonFlowController");
//        System.out.println("==== Bean 类型: " + bean.getClass().getName());
//
//        // 2. 打印这个类上所有注解
//        System.out.println("==== 类注解: ");
//        for (var ann : bean.getClass().getAnnotations()) {
//            System.out.println("  " + ann);
//        }
//
//        // 3. 打印 RequestMappingHandlerMapping 里注册的路径
//        Map<String, RequestMappingHandlerMapping> mappings = context.getBeansOfType(RequestMappingHandlerMapping.class);
//        mappings.forEach((name, mapping) -> {
//            mapping.getHandlerMethods().forEach((info, method) -> {
//                if (method.getBeanType().getSimpleName().equals("JsonFlowController")) {
//                    System.out.println("==== 映射路径: " + info + " -> " + method);
//                }
//            });
//        });
//    }

}
