package com.dra.order_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dra.order_service.config.property.ConsumerTopicProperties;
import com.dra.order_service.dto.event.EventWrapper;
import com.dra.order_service.dto.event.consumerEvent.OrderEventData;
import com.dra.order_service.enums.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final ConsumerTopicProperties consumerTopicProperties;

    @KafkaListener(topics="${app.topic.consumer.inventory.orderCreationSuccess}")
    public void subscribeOrderCreationSuccessEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.consumerTopicProperties.getInventory().getOrderCreationSuccess())){
            OrderEventData orderEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderEventData.class);
            this.orderService.changeStatusByEvents(
                orderEventData.getOrderId(), 
                OrderStatus.CREATION_SUCCESS, 
                orderEventData.getStatus()
            );
        }
    }

    @KafkaListener(topics="${app.topic.consumer.inventory.orderCreationFailed}")
    public void subscribeOrderCreationFailedEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.consumerTopicProperties.getInventory().getOrderCreationFailed())){
            OrderEventData orderEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderEventData.class);
            this.orderService.changeStatusByEvents(
                orderEventData.getOrderId(), 
                OrderStatus.CREATION_FAILED, 
                orderEventData.getStatus()
            );
        }
    }

    @KafkaListener(topics="${app.topic.consumer.inventory.orderCancellationSuccess}")
    public void subscribeOrderCancellationSuccessEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.consumerTopicProperties.getInventory().getOrderCancellationSuccess())){
            OrderEventData orderEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderEventData.class);
            this.orderService.changeStatusByEvents(
                orderEventData.getOrderId(), 
                OrderStatus.CANCELLATION_SUCCESS, 
                orderEventData.getStatus()
            );
        }
    }

    @KafkaListener(topics="${app.topic.consumer.inventory.orderCancellationFailed}")
    public void subscribeOrderCancellationFailedEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.consumerTopicProperties.getInventory().getOrderCancellationFailed())){
            OrderEventData orderEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderEventData.class);
            this.orderService.changeStatusByEvents(
                orderEventData.getOrderId(), 
                OrderStatus.CANCELLATION_FAILED, 
                orderEventData.getStatus()
            );
        }
    }

    @KafkaListener(topics="${app.topic.consumer.inventory.orderStatusChanged}")
    public void subscribeOrderStatusChangedEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.consumerTopicProperties.getInventory().getOrderStatusChanged())){
            OrderEventData orderEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderEventData.class);
            this.orderService.changeStatusByEvents(
                orderEventData.getOrderId(), 
                null, 
                orderEventData.getStatus()
            );
    }
        }

}