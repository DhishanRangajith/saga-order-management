package com.dra.order_service.enums;

public enum OrderStatus {
    CREATED, 
    CANCELLED,
    INVENTORY_CANCELLED, 
    PAYMENT_CANCELLED, 
    INVENTORY_RESERVED, 
    PAYMENT_SUCCESS, 
    COMPLETED
}
