package com.dra.inventory_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationData{
    private String productCode;
    private String productName;
    private Double price;
    private Double quantity;
}

