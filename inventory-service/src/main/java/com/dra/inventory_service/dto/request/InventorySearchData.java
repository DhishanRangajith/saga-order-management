package com.dra.inventory_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventorySearchData {

    private int page;
    private int pageSize;
    private String productName;
    private String productCode;
    private String productStatus;

}
