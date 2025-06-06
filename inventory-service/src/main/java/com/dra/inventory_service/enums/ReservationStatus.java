package com.dra.inventory_service.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    RESERVED_AWAITING_PAYMENT("Reserved - Awaiting payment"),
    RESERVED_PAYMENT_COMPLETED("Reserved - Payment completed"),

    RELEASED_PAYMENT_FAILED("Released - Payment not received in time"),
    RELEASED_ORDER_CANCELLED("Released - Order was cancelled"),

    NOT_ENOUGHT_INVENTORY("Failed - Not enought quantity"),
    INVALID_DATA("Failed - Invelid data"),

    FULFILLED_DELIVERED("Fulfilled - Items delivered");
    

    private String label;

    ReservationStatus(String label){
        this.label = label;
    }

}
