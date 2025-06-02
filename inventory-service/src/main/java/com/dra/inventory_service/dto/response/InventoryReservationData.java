package com.dra.inventory_service.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryReservationData {

    private Long orderId;
    private String productName;
    private String productCode;
    private Double quantity;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
