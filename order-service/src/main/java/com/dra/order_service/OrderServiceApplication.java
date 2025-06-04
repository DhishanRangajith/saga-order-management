package com.dra.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dra.order_service.config.property.ConsumerTopicProperties;
import com.dra.order_service.config.property.ProducerTopicProperties;

@SpringBootApplication
@EnableConfigurationProperties({ProducerTopicProperties.class, ConsumerTopicProperties.class})
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
