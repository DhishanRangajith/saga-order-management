package com.dra.order_service.dto.event.publisherEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDataEventData {
    
    private Long productCode;
    private Double quantity;

}
