package com.dra.inventory_service.annotation.validator;

import java.lang.reflect.Method;
import com.dra.inventory_service.annotation.IdMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdMatchValidator implements ConstraintValidator<IdMatches, Object[]>{

    @Override
    public boolean isValid(Object[] args, ConstraintValidatorContext context) {
        if (args == null || args.length < 2) {
            return false;
        }

        Long id = (Long) args[0];
        Object data = (Object) args[1];

        if (id == null || data == null) {
            return false;
        }

        try {
            Method getIdMethod = data.getClass().getMethod("getId");
            Object dtoId = getIdMethod.invoke(data);

            return id.equals(dtoId);
        } catch (Exception e) {
            return false;
        }
    }

}
