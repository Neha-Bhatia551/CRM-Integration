package com.crmintegration.demo.crmintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrmIntegration {

	public static void main(String[] args) {
		SpringApplication.run(CrmIntegration.class, args);
	}

}
