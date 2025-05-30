package com.dra.inventory_service.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {

    private Long id;
    private ProductData product;
    private Double quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
