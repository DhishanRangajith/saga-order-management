package com.dra.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.dra.inventory_service.config.property.KafkaConsumeTopicProperty;
import com.dra.inventory_service.config.property.KafkaProduceTopicProperty;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProduceTopicProperty.class, KafkaConsumeTopicProperty.class})
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
