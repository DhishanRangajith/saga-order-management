package com.dra.inventory_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductStatus {

    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static ProductStatus fromString(String value) {
        if(value == null) return null;
        for(ProductStatus productStatus : ProductStatus.values()){
            if(productStatus.name().equalsIgnoreCase(value)) return productStatus;
        }
        throw new IllegalArgumentException("Invalid ProductStatus value: " + value);
    }

}
