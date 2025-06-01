package com.dra.payment_service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.dra.payment_service.annotation.validator.OrderIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = OrderIdValidator.class)
public @interface ValidOrderId {
    String message() default "Id in parameter and data's id must match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
