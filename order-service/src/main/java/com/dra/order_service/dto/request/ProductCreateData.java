package com.dra.order_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateData {
    @NotNull(message = "Product name is required.")
    @NotBlank(message = "Product name is not to be blank.")
    private String name;

    @NotNull(message = "Product code is required.")
    @NotBlank(message = "Product code is not to be blank.")
    private String productCode;

    @NotNull(message = "Quantity is required.")
    private Double quantity;

    @NotNull(message = "Price is required.")
    private Double price;
}

    