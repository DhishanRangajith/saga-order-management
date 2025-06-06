package com.dra.payment_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.dra.payment_service.config.properties.KafkaProducerTopicProperty;
import com.dra.payment_service.dto.event.EventWrapper;
import com.dra.payment_service.dto.event.publisher.PaymentEventData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;
    private final KafkaProducerTopicProperty topicProperty;

    public void publishPaymentSuccessEvent(PaymentEventData paymentEventData){
        String topic = this.topicProperty.getPaymentSuccess();
        EventWrapper eventWrapper = new EventWrapper(topic, paymentEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishPaymentFailedEvent(PaymentEventData paymentEventData){
        String topic = this.topicProperty.getPaymentFailed();
        EventWrapper eventWrapper = new EventWrapper(topic, paymentEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishPaymentCancelledEvent(PaymentEventData paymentEventData){
        String topic = this.topicProperty.getPaymentCancelled();
        EventWrapper eventWrapper = new EventWrapper(topic, paymentEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

}
