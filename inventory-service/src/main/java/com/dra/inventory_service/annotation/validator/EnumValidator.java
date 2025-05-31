package com.dra.inventory_service.annotation.validator;

import java.util.Arrays;

import com.dra.inventory_service.annotation.EnumValid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>>{

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) { 
        return value == null || Arrays.asList(enumClass.getEnumConstants()).contains(value);
    }

}
