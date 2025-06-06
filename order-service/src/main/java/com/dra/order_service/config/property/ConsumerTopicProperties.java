package com.dra.order_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.consumer")
@Getter
@Setter
public class ConsumerTopicProperties {
    private Inventory inventory;

    @Getter
    @Setter
    public static class Inventory {
        String orderStatusChanged;
        String orderCreationSuccess;
        String orderCreationFailed;
        String orderCancellationSuccess;
        String orderCancellationFailed;
    }
}