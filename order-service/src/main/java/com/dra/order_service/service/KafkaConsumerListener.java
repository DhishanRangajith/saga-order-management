package com.dra.order_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.dra.order_service.dto.consumerEvent.OrderStatusUpdateEventData;
import com.dra.order_service.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final OrderService orderService;

    @KafkaListener(topics="${app.topic.consumer.inventory.orderStatusUpdate}")
    public void subscribeOrderStatusUpdate(OrderStatusUpdateEventData orderStatusUpdateEvent){
        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusUpdateEvent.getStatus());
        this.orderService.changeStatus(orderStatusUpdateEvent.getOrderId(), orderStatus);
    }

}
