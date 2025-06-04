package com.dra.payment_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dra.payment_service.config.properties.KafkaProducerTopicProperty;
import com.dra.payment_service.dto.event.EventWrapper;
import com.dra.payment_service.dto.event.publisher.PaymentFailRefundEventData;
import com.dra.payment_service.dto.event.publisher.PaymentSuccessEventData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;
    private final KafkaProducerTopicProperty topicProperty;

    public void publishPaymentSuccessEvent(PaymentSuccessEventData paymentSuccessEventData){
        EventWrapper eventWrapper = new EventWrapper(this.topicProperty.getPaymentSuccess(), paymentSuccessEventData);
        this.kafkaTemplate.send(this.topicProperty.getPaymentSuccess(), eventWrapper);
    }

    public void publishPaymentFailedEvent(PaymentFailRefundEventData paymentFailRefundEventData){
        EventWrapper eventWrapper = new EventWrapper(this.topicProperty.getPaymentFailed(), paymentFailRefundEventData);
        this.kafkaTemplate.send(this.topicProperty.getPaymentFailed(), eventWrapper);
    }

    public void publishPaymentRefundedEvent(PaymentFailRefundEventData paymentFailRefundEventData){
        EventWrapper eventWrapper = new EventWrapper(this.topicProperty.getPaymentRefund(), paymentFailRefundEventData);
        this.kafkaTemplate.send(this.topicProperty.getPaymentRefund(), eventWrapper);
    }

}
