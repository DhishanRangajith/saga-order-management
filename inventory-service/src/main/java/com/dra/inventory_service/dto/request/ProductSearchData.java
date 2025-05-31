package com.dra.inventory_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchData {
    private int page;
    private int pageSize;
    private String name;
    private String productCode;
    private String status;
}
