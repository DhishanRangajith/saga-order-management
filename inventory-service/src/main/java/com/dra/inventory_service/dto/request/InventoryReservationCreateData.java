package com.dra.inventory_service.dto.request;

import com.dra.inventory_service.annotation.ProductCodeValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReservationCreateData {

    @NotNull(message = "Product is required.")
    @NotBlank(message = "Product is required.")
    @ProductCodeValid
    private String productCode;

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity should be grater than 0.")
    private Double quantity;

}
