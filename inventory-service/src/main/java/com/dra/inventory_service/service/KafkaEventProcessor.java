package com.dra.inventory_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.dra.inventory_service.config.property.KafkaProduceTopicProperty;
import com.dra.inventory_service.dto.event.EventWrapper;
import com.dra.inventory_service.dto.event.publisher.OrderStatusUpdateEventData;
import com.dra.inventory_service.dto.event.publisher.ReservationEventData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor {

    private final KafkaTemplate<String, EventWrapper> kafkaTemplate;
    private final KafkaProduceTopicProperty kafkaProduceTopicProperty;

    public void publishOrderStatusUpdateEvent(OrderStatusUpdateEventData orderStatusUpdateEvent){
        EventWrapper eventWrapper = new EventWrapper(this.kafkaProduceTopicProperty.getOrderStatusUpdate(), orderStatusUpdateEvent);
        this.kafkaTemplate.send(this.kafkaProduceTopicProperty.getOrderStatusUpdate(), eventWrapper);
    }

    public void publishInventoryReleaseEvent(OrderStatusUpdateEventData orderStatusUpdateEvent){
        EventWrapper eventWrapper = new EventWrapper(this.kafkaProduceTopicProperty.getStockRelease(), orderStatusUpdateEvent);
        this.kafkaTemplate.send(this.kafkaProduceTopicProperty.getStockRelease(), eventWrapper);
    }

    public void publishReservationSuccessEvent(ReservationEventData reservationEventData){
        EventWrapper eventWrapper = new EventWrapper(this.kafkaProduceTopicProperty.getReserved(), reservationEventData);
        this.kafkaTemplate.send(this.kafkaProduceTopicProperty.getReserved(), eventWrapper);
    }

}
