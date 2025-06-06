package com.dra.payment_service.dto.request;

import com.dra.payment_service.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateData {

    @NotNull(message = "Status is invalid.")
    private PaymentStatus status;

}
