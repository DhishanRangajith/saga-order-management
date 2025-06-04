package com.dra.inventory_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix = "app.topic.consumer")
@Getter
@Setter
public class KafkaConsumeTopicProperty {
    private Order order;
    private Payment payment;

    @Getter
    @Setter
    public static class Order { //create=${app.topic.prefix}ORDER_CREATE
        private String create;
    }

    @Getter
    @Setter
    public static class Payment {
        private String paymentFail;
        private String paymentRefund;
        private String updatepaymentStatus;
    }
}



