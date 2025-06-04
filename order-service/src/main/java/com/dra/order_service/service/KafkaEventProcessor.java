package com.dra.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dra.order_service.config.property.ProducerTopicProperties;
import com.dra.order_service.dto.EventWrapper;
import com.dra.order_service.dto.publisherEvent.OrderCancelEventData;
import com.dra.order_service.dto.publisherEvent.OrderCreateEventData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final ProducerTopicProperties producerTopicProperties;
    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreateEventData orderCreateEvent){
        EventWrapper eventWrapper = new EventWrapper(this.producerTopicProperties.getOrder().getCreate(), orderCreateEvent);
        this.kafkaTemplate.send(this.producerTopicProperties.getOrder().getCreate(), eventWrapper);
    }

    public void publishOrderCancelledEvent(OrderCancelEventData orderCancelEvent){
        EventWrapper eventWrapper = new EventWrapper(this.producerTopicProperties.getOrder().getCancel(), orderCancelEvent);
        this.kafkaTemplate.send(this.producerTopicProperties.getOrder().getCancel(), eventWrapper);
    }

}
