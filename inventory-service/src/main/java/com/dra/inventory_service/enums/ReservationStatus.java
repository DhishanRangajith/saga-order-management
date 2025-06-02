package com.dra.inventory_service.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    RESERVED_AWAITING_PAYMENT("Reserved - Awaiting payment"),
    RESERVED_PAYMENT_COMPLETED("Reserved - Payment completed"),

    RELEASED_PAYMENT_NOT_RECEIVED("Released - Payment not received in time"),
    RELEASED_ORDER_CANCELLED("Released - Order was cancelled"),

    FULFILLED_DELIVERED("Fulfilled - Items delivered");

    private String label;

    ReservationStatus(String label){
        this.label = label;
    }

}
