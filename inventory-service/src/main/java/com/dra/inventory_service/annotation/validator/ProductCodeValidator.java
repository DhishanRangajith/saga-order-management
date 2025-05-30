package com.dra.inventory_service.annotation.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dra.inventory_service.annotation.ProductCreateValid;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.enums.ProductStatus;
import com.dra.inventory_service.repository.ProductRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCodeValidator implements ConstraintValidator<ProductCreateValid, String>{

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null || value.isBlank()){
            return false;
        }

        boolean valid = true;
        Optional<ProductEntity> productOptional = this.productRepository.findByProductCode(value);

        if(productOptional.isEmpty()){
            context.buildConstraintViolationWithTemplate("Product is not found.")
                    .addPropertyNode("productCode").addConstraintViolation();
            valid = false;
        }else if(productOptional.get().getStatus().equals(ProductStatus.INACTIVE)){
            context.buildConstraintViolationWithTemplate("Product is inactive.")
                    .addPropertyNode("productCode").addConstraintViolation();
            valid = false;
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
        }

        return valid;
    }

}
