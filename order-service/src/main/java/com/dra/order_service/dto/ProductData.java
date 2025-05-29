package com.dra.order_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductData {
    
    private Long id;
    private Long productCode;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private List<OrderData> orders;
}
