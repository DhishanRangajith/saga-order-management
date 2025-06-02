package com.dra.order_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderSearchData {

    private Long id;
    private String productCode;
    private String status;
    private int page;
    private int pageSize;

}
