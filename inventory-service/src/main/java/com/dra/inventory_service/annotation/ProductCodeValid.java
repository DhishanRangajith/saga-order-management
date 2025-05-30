package com.dra.inventory_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.dra.inventory_service.annotation.validator.ProductCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductCodeValidator.class)
public @interface ProductCodeValid {
    String message() default "Product code is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
