package com.dra.payment_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dra.payment_service.dto.event.EventWrapper;
import com.dra.payment_service.dto.event.consumer.OrderCancelEventData;
import com.dra.payment_service.dto.event.consumer.ReservationSuccessEventData;
import com.dra.payment_service.dto.request.PaymentCreateData;
import com.dra.payment_service.dto.request.PaymentUpdateData;
import com.dra.payment_service.enums.PaymentStatus;
import com.dra.payment_service.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerListner {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @KafkaListener(topics = {"${app.topic.consumer.order.orderCancel}"}, groupId = "fff")
    public void consumOrderCancelEvent(EventWrapper eventWrapper){
        if(eventWrapper.getData() instanceof OrderCancelEventData orderCancelEventData){
            PaymentUpdateData paymentUpdateData = new PaymentUpdateData();
            paymentUpdateData.setStatus(PaymentStatus.CANCELLED_ORDER_CANCELLED);
            this.paymentService.updatePaymentStatus(orderCancelEventData.getOrderId(), paymentUpdateData);
        }
    }

    @KafkaListener(topics = {"${app.topic.consumer.inventory.reservationSuccess}"}, groupId = "fff")
    public void consumReservationSuccessEvent(EventWrapper eventWrapper){
        if(eventWrapper.getData() instanceof ReservationSuccessEventData reservationSuccessEventData){
            PaymentCreateData paymentCreateData = this.paymentMapper.toPaymentCreateData(reservationSuccessEventData);
            this.paymentService.createPayment(paymentCreateData);
        }
    }

}
