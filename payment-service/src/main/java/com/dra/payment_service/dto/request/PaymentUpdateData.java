package com.dra.payment_service.dto.request;

import com.dra.payment_service.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentUpdateData {

    @NotNull(message = "Status is invalid.")
    private PaymentStatus status;

}
