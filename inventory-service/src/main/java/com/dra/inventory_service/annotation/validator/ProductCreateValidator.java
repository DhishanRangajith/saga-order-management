package com.dra.inventory_service.annotation.validator;

import com.dra.inventory_service.annotation.ProductCreateValid;
import com.dra.inventory_service.dto.ProductData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductCreateValidator implements ConstraintValidator<ProductCreateValid, ProductData>{

    @Override
    public boolean isValid(ProductData value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getName() == null || value.getName().isBlank()) {
            context.buildConstraintViolationWithTemplate("Name is required")
                    .addPropertyNode("name").addConstraintViolation();
            valid = false;
        }

        if (value.getProductCode() == null || value.getProductCode().isBlank()) {
            context.buildConstraintViolationWithTemplate("Product code is required")
                    .addPropertyNode("productCode").addConstraintViolation();
            valid = false;
        }

        if (value.getPrice() == null || value.getPrice() <= 0) {
            context.buildConstraintViolationWithTemplate("Price must be > 0")
                    .addPropertyNode("price").addConstraintViolation();
            valid = false;
        }

        if (value.getStatus() == null) {
            context.buildConstraintViolationWithTemplate("Product status is required or valid")
                    .addPropertyNode("status").addConstraintViolation();
            valid = false;
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
        }

        return valid;
    }

}
