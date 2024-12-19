package com.pig4cloud.pigx.jsonflow;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnableOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 工作流模块业务管理模块
 *
 * @author luolin
 * @date 2020/2/14
 */
@EnableOpenApi("jsonflow")
@EnablePigxFeignClients
@EnablePigxResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class PigxJsonFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxJsonFlowApplication.class, args);
	}

}
