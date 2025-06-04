package com.dra.order_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.producer")
@Getter
@Setter
public class ProducerTopicProperties {
    private Order order;

    @Getter
    @Setter
    public static class Order {
        String create;
        String cancel;
    }
}
