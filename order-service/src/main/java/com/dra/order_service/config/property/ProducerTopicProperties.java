package com.dra.order_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.producer.order")
@Getter
@Setter
public class ProducerTopicProperties {
    private String creationRequest;
    private String cancellationRequest;
}