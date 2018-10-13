package com.tgt.myretail.products.api;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.slf4j.Logger;

@Configuration
@EnableRetry
@SpringBootApplication
public class ProductsApiApplication {
	private static Logger LOGGER = LoggerFactory.getLogger(ProductsApiApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(ProductsApiApplication.class, args);
	}
}
