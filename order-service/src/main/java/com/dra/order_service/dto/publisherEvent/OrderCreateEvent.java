package com.dra.order_service.dto.publisherEvent;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateEvent {

    private Long orderId;
    private List<ProductDataEvent> products;

}
