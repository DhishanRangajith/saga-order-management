package com.dra.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import com.dra.order_service.config.property.ProducerTopicProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ProducerTopicConfig {

    private final ProducerTopicProperties producerTopicProperties;

    @Bean
    public NewTopic createOrderTopic(){
        return TopicBuilder.name(this.producerTopicProperties.getCreationRequest())
                            // .partitions(1)
                            // .replicas(1)
                            .build();
    }

    @Bean
    public NewTopic cancelOrderTopic(){
        return TopicBuilder.name(this.producerTopicProperties.getCancellationRequest()).build();
    }

}
