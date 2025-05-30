package com.dra.inventory_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dra.inventory_service.annotation.validator.IdMatchValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = IdMatchValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdMatches {
    String message() default "Id in parameter and data's id must match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
