package com.dra.inventory_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dra.inventory_service.config.property.KafkaConsumeTopicProperty;
import com.dra.inventory_service.dto.event.EventWrapper;
import com.dra.inventory_service.dto.event.consumer.OrderCreateEventData;
import com.dra.inventory_service.dto.event.consumer.PaymentFailRefundEventData;
import com.dra.inventory_service.dto.event.consumer.PaymentSuccessEventData;
import com.dra.inventory_service.dto.request.CancelReservationData;
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

    @KafkaListener(topics = "${app.topic.consumer.order.create}", groupId = "fff") // default groupId = "inventory_service" override to fff
    public void consumeCreateOrderEvent(EventWrapper eventWrapper){
        if (eventWrapper.getEventType().equals(this.kafkaConsumeTopicProperty.getOrder().getCreate())) {
            ObjectMapper objectMapper = new ObjectMapper();
            OrderCreateEventData orderCreateEventData = objectMapper.convertValue(eventWrapper.getData(), OrderCreateEventData.class);
            ReservationCreateData reservationCreateData = this.reservationMapper.toReservationCreateDataFromEvent(orderCreateEventData);
            this.reservationService.createReservation(reservationCreateData);
        }
    }

    @KafkaListener(topics = {"${app.topic.consumer.payment.paymentRefund}", "${app.topic.consumer.payment.paymentFail}"})
    public void consumePaymentRefundOrFailEvent(EventWrapper eventWrapper){
        if(eventWrapper.getData() instanceof PaymentFailRefundEventData paymentFailRefundEventData){
            Long orderId = paymentFailRefundEventData.getOrderId();
            ReservationStatus status = ReservationStatus.RELEASED_ORDER_CANCELLED;
            CancelReservationData cancelReservationData = new CancelReservationData();
            cancelReservationData.setStatus(status);
            this.reservationService.cancelReservation(orderId, cancelReservationData);
        }
    }

    @KafkaListener(topics = "${app.topic.consumer.payment.updatepaymentStatus}")
    public void consumePaymentSuccessEvent(EventWrapper eventWrapper){
        if(eventWrapper.getData() instanceof PaymentSuccessEventData paymentStatusUpdateEventData){
            // Long orderId = paymentStatusUpdateEventData.getOrderId();
        }
    }

}
