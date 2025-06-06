package com.dra.order_service.dto.event.publisherEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancelEventData {

    private Long orderId;

}
