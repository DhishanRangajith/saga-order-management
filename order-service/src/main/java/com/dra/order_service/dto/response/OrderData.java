package com.dra.order_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import com.dra.order_service.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {

    private Long id;
    private OrderStatus status;
    private String subStatus;
    private Double amount;
    private List<ProductData> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
