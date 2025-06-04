package com.dra.order_service.dto.publisherEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDataEvent {
    
    private Long productCode;
    private Double quantity;

}
