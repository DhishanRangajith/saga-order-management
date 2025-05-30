package com.dra.inventory_service.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantData {

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Double quantity;
}
