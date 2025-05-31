package com.dra.inventory_service.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryReservationData {

    private Long orderId;
    private ProductData product;
    private Double quantity;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
