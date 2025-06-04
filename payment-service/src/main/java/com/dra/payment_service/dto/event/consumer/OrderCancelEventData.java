package com.dra.payment_service.dto.event.consumer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancelEventData {

    private Long orderId;

}
