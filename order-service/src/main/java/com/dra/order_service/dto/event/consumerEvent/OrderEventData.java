package com.dra.order_service.dto.event.consumerEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderEventData {

    private Long orderId;
    private String status;

}
