package com.dra.inventory_service.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.topic.producer.inventory")
@Getter
@Setter
public class KafkaProduceTopicProperty {

    private String orderStatusUpdate;
    private String reserved;
    private String stockRelease;

}