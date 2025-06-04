package com.dra.order_service.dto.consumerEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderStatusUpdateEvent {

    private Long orderId;
    private String status;

}
