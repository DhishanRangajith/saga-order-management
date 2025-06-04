package com.dra.payment_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {

    PENDING,
    FAILED_RETRY_POSSIBLE,
    CANCELLED_ORDER_CANCELLED,
    COMPLETED;

    @JsonCreator
    public static PaymentStatus fromString(String value) {
        try{
            return PaymentStatus.valueOf(value.toUpperCase());
        }catch(Exception exp){
            return null;
        }
    }

}
