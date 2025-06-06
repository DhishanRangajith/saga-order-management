package com.dra.inventory_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.dra.inventory_service.config.property.KafkaConsumeTopicProperty;
import com.dra.inventory_service.dto.event.EventWrapper;
import com.dra.inventory_service.dto.event.consumer.OrderCreateEventData;
import com.dra.inventory_service.dto.event.consumer.PaymentEventData;
import com.dra.inventory_service.dto.request.ReservationCreateData;
import com.dra.inventory_service.enums.ReservationStatus;
import com.dra.inventory_service.mapper.ReservationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final KafkaConsumeTopicProperty kafkaConsumeTopicProperty;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.topic.consumer.order.creationRequest}", groupId = "fff") // default groupId = "inventory_service" override to fff
    public void consumeOrderCreationRequestEvent(EventWrapper eventWrapper){
        if (eventWrapper.getEventType().equals(this.kafkaConsumeTopicProperty.getOrder().getCreationRequest())) {
            OrderCreateEventData orderCreateEventData = this.objectMapper.convertValue(eventWrapper.getData(), OrderCreateEventData.class);
            ReservationCreateData reservationCreateData = this.reservationMapper.toReservationCreateDataFromEvent(orderCreateEventData);
            this.reservationService.createReservation(reservationCreateData);
        }
    }

    @KafkaListener(topics = {"${app.topic.consumer.payment.paymentFailed}"})
    public void consumePaymentFailedEvent(EventWrapper eventWrapper){
        if (eventWrapper.getEventType().equals(this.kafkaConsumeTopicProperty.getPayment().getPaymentFailed())) {
            PaymentEventData paymentEventData = this.objectMapper.convertValue(eventWrapper.getData(), PaymentEventData.class);
            this.reservationService.releaseReservationItems(paymentEventData.getOrderId(), ReservationStatus.RELEASED_PAYMENT_FAILED);
        }
    }

    @KafkaListener(topics = {"${app.topic.consumer.payment.paymentCancelled}"})
    public void consumePaymentCancelledEvent(EventWrapper eventWrapper){
        if (eventWrapper.getEventType().equals(this.kafkaConsumeTopicProperty.getPayment().getPaymentCancelled())) {
            PaymentEventData paymentEventData = this.objectMapper.convertValue(eventWrapper.getData(), PaymentEventData.class);
            this.reservationService.releaseReservationItems(paymentEventData.getOrderId(), ReservationStatus.RELEASED_ORDER_CANCELLED);
        }
    }

    @KafkaListener(topics = "${app.topic.consumer.payment.paymentSuccess}")
    public void consumePaymentSuccessEvent(EventWrapper eventWrapper){
        if (eventWrapper.getEventType().equals(this.kafkaConsumeTopicProperty.getPayment().getPaymentSuccess())) {
            PaymentEventData paymentEventData = this.objectMapper.convertValue(eventWrapper.getData(), PaymentEventData.class);
            this.reservationService.setPaymentSuccessToReservation(paymentEventData.getOrderId());
        }
    }

}
