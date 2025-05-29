package com.dra.order_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dra.order_service.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {

    private Long id;

    @NotNull(message = "Products are required.")
    private List<ProductData> products;
    
    private double quantity;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
