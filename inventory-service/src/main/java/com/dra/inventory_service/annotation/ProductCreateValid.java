package com.dra.inventory_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dra.inventory_service.annotation.validator.ProductCreateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductCreateValidator.class)
public @interface ProductCreateValid {
    String message() default "Invalid Product Data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
