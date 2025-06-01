package com.dra.payment_service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dra.payment_service.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentData {

    private UUID id;
    private Long orderId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
