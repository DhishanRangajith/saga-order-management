package com.dra.order_service.dto.publisherEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancelEvent {

    private Long orderId;
    private String reason;

}
