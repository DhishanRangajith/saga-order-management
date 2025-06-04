package com.dra.payment_service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.producer.payment")
@Getter
@Setter
public class KafkaProducerTopicProperty {
    private String paymentSuccess;
    private String paymentFailed;
    private String paymentRefund;
}
