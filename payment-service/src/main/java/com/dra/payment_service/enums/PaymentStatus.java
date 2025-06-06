package com.dra.payment_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {

    PENDING,
    FAILED,
    CANCELLED_ORDER_CANCELLED,
    CANCELLED_ORDER_CANCELLED_REFUND,
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
