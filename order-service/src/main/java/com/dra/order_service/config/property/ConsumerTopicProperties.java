package com.dra.order_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.consumer")
@Getter
@Setter
public class ConsumerTopicProperties {
    private Inventory inventory;
    private Payment payment;

    @Getter
    @Setter
    public static class Inventory {
        String status_update;
        String stock_released;
    }

    @Getter
    @Setter
    public static class Payment {
        String status_update;
        String fail_cancel;
    }
}