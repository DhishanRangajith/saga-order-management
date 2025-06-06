package com.dra.inventory_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.dra.inventory_service.config.property.KafkaProduceTopicProperty;
import com.dra.inventory_service.dto.event.EventWrapper;
import com.dra.inventory_service.dto.event.publisher.OrderEventData;
import com.dra.inventory_service.dto.event.publisher.ReservationEventData;
import com.dra.inventory_service.enums.ReservationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;
    private final KafkaProduceTopicProperty kafkaProduceTopicProperty;

    public void publishReservationCreatedEvent(ReservationEventData reservationEventData){
        String topic = this.kafkaProduceTopicProperty.getReservationCreated();
        EventWrapper eventWrapper = new EventWrapper(topic, reservationEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderStatusChangedEvent(OrderEventData orderEventData){
        String topic = this.kafkaProduceTopicProperty.getOrderStatusChanged();
        EventWrapper eventWrapper = new EventWrapper(topic, orderEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderCreationSuccessEvent(OrderEventData orderEventData){
         String topic = this.kafkaProduceTopicProperty.getOrderCreationSuccess();
        EventWrapper eventWrapper = new EventWrapper(topic, orderEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderCreationFailedEvent(Long orderId, ReservationStatus reservationStatus){
        String topic = this.kafkaProduceTopicProperty.getOrderCreationFailed();
        EventWrapper eventWrapper = new EventWrapper(topic, new OrderEventData(orderId, reservationStatus.name()));
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderCancellationSuccessEvent(OrderEventData orderEventData){
         String topic = this.kafkaProduceTopicProperty.getOrderCancellationSuccess();
        EventWrapper eventWrapper = new EventWrapper(topic, orderEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

    public void publishOrderCancellationFailedEvent(OrderEventData orderEventData){
         String topic = this.kafkaProduceTopicProperty.getOrderCancellationFailed();
        EventWrapper eventWrapper = new EventWrapper(topic, orderEventData);
        this.kafkaTemplate.send(topic, eventWrapper);
    }

}
