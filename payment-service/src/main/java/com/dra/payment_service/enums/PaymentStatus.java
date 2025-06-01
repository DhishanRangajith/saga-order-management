package com.dra.payment_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {

    PENDING,
    SUCCESS,
    FAILED,
    CANCELED;

    @JsonCreator
    public static PaymentStatus fromString(String value) {
        try{
            return PaymentStatus.valueOf(value.toUpperCase());
        }catch(Exception exp){
            return null;
        }
    }

}
