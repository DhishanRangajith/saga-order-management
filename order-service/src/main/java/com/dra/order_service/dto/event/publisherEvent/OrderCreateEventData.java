package com.dra.order_service.dto.event.publisherEvent;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateEventData {

    private Long orderId;
    private List<ProductDataEventData> products;

}
