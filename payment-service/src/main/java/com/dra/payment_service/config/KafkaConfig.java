package com.dra.payment_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import com.dra.payment_service.config.properties.KafkaProducerTopicProperty;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProducerTopicProperty kafkaProducerTopicProperty;

    @Bean
    public NewTopic newPaymentSuccessTopic(){
        return TopicBuilder.name(this.kafkaProducerTopicProperty.getPaymentSuccess()).build();
    }

    @Bean
    public NewTopic newPaymentFailedTopic(){
        return TopicBuilder.name(this.kafkaProducerTopicProperty.getPaymentFailed()).build();
    }

    @Bean
    public NewTopic newPaymentRefundTopic(){
        return TopicBuilder.name(this.kafkaProducerTopicProperty.getPaymentRefund()).build();
    }

}
