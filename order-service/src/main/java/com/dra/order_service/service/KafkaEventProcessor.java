package com.dra.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dra.order_service.config.property.ProducerTopicProperties;
import com.dra.order_service.dto.event.EventWrapper;
import com.dra.order_service.dto.event.publisherEvent.OrderCancelEventData;
import com.dra.order_service.dto.event.publisherEvent.OrderCreateEventData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final ProducerTopicProperties producerTopicProperties;
    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreateEventData orderCreateEvent){
        String topic = this.producerTopicProperties.getCreationRequest();
        EventWrapper eventWrapper = new EventWrapper(topic, orderCreateEvent);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderCancelledEvent(OrderCancelEventData orderCancelEvent){
        String topic = this.producerTopicProperties.getCancellationRequest();
        EventWrapper eventWrapper = new EventWrapper(topic, orderCancelEvent);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

}
