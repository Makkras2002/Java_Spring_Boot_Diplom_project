package com.makkras.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories()
@EnableScheduling
public class ShopApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}
