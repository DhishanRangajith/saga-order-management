package com.dra.order_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductData {
    
    private Long productCode;
    private String name;
    private Double price;
    private Double quantity;

}
