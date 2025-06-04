package com.dra.payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.dra.payment_service.config.properties.KafkaConsumerTopicProperty;
import com.dra.payment_service.config.properties.KafkaProducerTopicProperty;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProducerTopicProperty.class, KafkaConsumerTopicProperty.class})
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
