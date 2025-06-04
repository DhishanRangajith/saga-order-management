package com.dra.inventory_service.dto.event.consumer;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateEventData {

    private Long orderId;
    private List<ProductDataEventData> products;

}
