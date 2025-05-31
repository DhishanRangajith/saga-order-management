package com.dra.inventory_service.dto.request;

import com.dra.inventory_service.annotation.EnumValid;
import com.dra.inventory_service.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductData {

    @NotBlank(message = "Product name is not to be empty.")
    private String name;

    @EnumValid(enumClass = ProductStatus.class)
    private ProductStatus status;

    @Min(value = 0, message = "Prive is grater equal or grater than 0.")
    private Double price;

}
