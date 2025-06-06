package com.dra.payment_service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.consumer")
@Getter
@Setter
public class KafkaConsumerTopicProperty {

    private Order order;
    private Inventory inventory;

    @Getter
    @Setter
    public static class Order {
        private String cancellationRequest;
    }

    @Getter
    @Setter
    public static class Inventory {
        private String reservationCreated;
    }

}
