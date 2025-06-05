package com.dra.inventory_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;

import com.dra.inventory_service.config.property.KafkaProduceTopicProperty;
import com.dra.inventory_service.dto.event.EventWrapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProduceTopicProperty kafkaProduceTopicProperty;

    @Bean
    public NewTopic updateOrderStatusTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getOrderStatusUpdate()).build();
    }

    @Bean
    public NewTopic inventoryReservedTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getReserved()).build();
    }

    @Bean
    public NewTopic inventoryStockRelaeseTopic(){
        return TopicBuilder.name(this.kafkaProduceTopicProperty.getStockRelease()).build();
    }

}
