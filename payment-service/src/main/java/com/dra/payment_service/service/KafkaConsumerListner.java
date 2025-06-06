package com.dra.payment_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dra.payment_service.config.properties.KafkaConsumerTopicProperty;
import com.dra.payment_service.dto.event.EventWrapper;
import com.dra.payment_service.dto.event.consumer.OrderCancelEventData;
import com.dra.payment_service.dto.event.consumer.ReservationSuccessEventData;
import com.dra.payment_service.dto.request.PaymentCreateData;
import com.dra.payment_service.dto.request.PaymentUpdateData;
import com.dra.payment_service.enums.PaymentStatus;
import com.dra.payment_service.mapper.PaymentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerListner {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final ObjectMapper objectMapper;
    private final KafkaConsumerTopicProperty kafkaConsumerTopicProperty;

    @KafkaListener(topics = {"${app.topic.consumer.order.cancellationRequest}"}, groupId = "fff")
    public void consumOrderCancellationRequestEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.kafkaConsumerTopicProperty.getOrder().getCancellationRequest())){
            OrderCancelEventData orderCancelEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderCancelEventData.class);
            this.paymentService.updatePaymentStatus(orderCancelEventData.getOrderId(), new PaymentUpdateData(PaymentStatus.CANCELLED_ORDER_CANCELLED), true);
        }
    }

    @KafkaListener(topics = {"${app.topic.consumer.inventory.reservationCreated}"}, groupId = "fff")
    public void consumInventoryReservationCreatedEvent(EventWrapper eventWrapper){
        if(eventWrapper.getEventType().equals(this.kafkaConsumerTopicProperty.getInventory().getReservationCreated())){
            ReservationSuccessEventData reservationEventData = this.objectMapper.convertValue(eventWrapper.getData(), ReservationSuccessEventData.class);
            PaymentCreateData paymentCreateData = this.paymentMapper.toPaymentCreateData(reservationEventData);
            this.paymentService.createPayment(paymentCreateData);
        }
    }

}
