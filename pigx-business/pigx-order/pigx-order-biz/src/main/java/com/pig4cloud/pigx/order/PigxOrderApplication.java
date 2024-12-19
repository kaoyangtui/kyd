package com.pig4cloud.pigx.order;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnableOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 工单模块业务管理模块
 *
 * @author luolin
 * @date 2019年06月21日
 */
@EnableOpenApi("order")
@EnablePigxFeignClients
@EnablePigxResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class PigxOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxOrderApplication.class, args);
	}

}
