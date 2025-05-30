package com.dra.inventory_service.dto.request;

import com.dra.inventory_service.annotation.ProductCodeValid;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReservationData {

    @NotNull(message = "Order is is required.")
    private Long orderId;

    @ProductCodeValid
    private String productCode;

    @NotNull(message = "Quantity is is required.")
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Double quantity;

}


