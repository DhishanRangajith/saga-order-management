package com.dra.inventory_service.dto.event.consumer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentFailRefundEventData {

    private Long orderId;
    private String reason;

}
