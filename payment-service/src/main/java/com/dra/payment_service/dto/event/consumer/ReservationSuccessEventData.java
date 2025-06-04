package com.dra.payment_service.dto.event.consumer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationSuccessEventData {

    private Long orderId;
    private Double amount;

}
