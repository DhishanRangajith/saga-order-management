package com.dra.inventory_service.dto.event.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateEventData {

    private Long orderId;
    private String status;

}
