package com.dra.inventory_service.dto;

import java.time.LocalDateTime;
import com.dra.inventory_service.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {

    private Long id;

    @NotBlank(message = "Product name is not to be empty.")
    private String name;

    @NotNull(message = "Product code is required.")
    @NotBlank(message = "Product code is not to be empty.")
    private String productCode;

    private ProductStatus status;

    @Min(0)
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
