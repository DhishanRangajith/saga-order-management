package com.dra.inventory_service.dto;

import java.time.LocalDateTime;
import com.dra.inventory_service.enums.InventoryReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryReservationData {

    private Long id;
    private Long orderId;
    private ProductData product;
    private Double quantity;
    private Double price;
    private InventoryReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
