package com.cts.engagement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.cts"})
@EnableFeignClients
public class EngagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EngagementServiceApplication.class, args);
	}

}
