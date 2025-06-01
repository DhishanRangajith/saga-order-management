package com.dra.payment_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentSearchData {

    private Long orderId;
    private String status;
    private int page;
    private int pageSize;

}
