package com.dra.inventory_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.dra.inventory_service.annotation.validator.ProductUpdateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductUpdateValidator.class)
public @interface ProductUpdateValid {
    String message() default "Invalid update product data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
