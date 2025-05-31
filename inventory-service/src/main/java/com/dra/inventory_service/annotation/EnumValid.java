package com.dra.inventory_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dra.inventory_service.annotation.validator.EnumValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value. Must match one of the enum values.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
