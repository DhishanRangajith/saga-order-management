package com.dra.order_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dra.order_service.enums.ProductStatus;

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
    private ProductStatus status;
    // private List<OrderData> orders;
}
