package com.dra.inventory_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationSearchData {

    private Long orderId;
    private String productCode;
    private String status;
    private int page;
    private int pageSize;

}
