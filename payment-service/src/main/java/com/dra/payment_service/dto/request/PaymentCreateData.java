package com.dra.payment_service.dto.request;

import com.dra.payment_service.annotation.ValidOrderId;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentCreateData {

    @NotNull(message = "Order id is required.")
    @ValidOrderId
    private Long orderId;

    @NotNull(message = "Amount is required.")
    @Min(value = 1, message = "Amount should be grater than 0.")
    private Double amount;

}
