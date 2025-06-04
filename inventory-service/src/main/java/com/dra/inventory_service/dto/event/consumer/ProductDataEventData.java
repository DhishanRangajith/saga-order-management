package com.dra.inventory_service.dto.event.consumer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDataEventData {
    
    private String productCode;
    private Double quantity;

}
