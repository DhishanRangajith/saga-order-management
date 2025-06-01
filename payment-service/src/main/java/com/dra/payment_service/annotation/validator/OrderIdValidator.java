package com.dra.payment_service.annotation.validator;

import org.springframework.stereotype.Component;
import com.dra.payment_service.annotation.ValidOrderId;
import com.dra.payment_service.repository.PaymentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderIdValidator implements ConstraintValidator<ValidOrderId, Long>{

    private final PaymentRepository paymentRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isExists = this.paymentRepository.existsByOrderId(value);
        boolean valid = true;
        if(isExists){
            context.buildConstraintViolationWithTemplate("Order is already exists.")
                    .addPropertyNode("OrderId").addConstraintViolation();
            valid = false;
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
        }
        return valid;
    }

}
