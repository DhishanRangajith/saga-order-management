package com.dra.inventory_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import com.dra.inventory_service.config.property.KafkaProduceTopicProperty;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProduceTopicProperty kafkaProduceTopicProperty;

    @Bean
    public NewTopic orderReservationCreatedTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getReservationCreated()).build();
    }

    @Bean
    public NewTopic orderStatusChangedTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderStatusChanged()).build();
    }

    @Bean
    public NewTopic orderCreationSuccessTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderCreationSuccess()).build();
    }

    @Bean
    public NewTopic orderCreationFailTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderCreationFailed()).build();
    }

    @Bean
    public NewTopic orderCancellationSuccessTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderCancellationSuccess()).build();
    }

    @Bean
    public NewTopic orderCancellationFailedTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderCancellationFailed()).build();
    }

}