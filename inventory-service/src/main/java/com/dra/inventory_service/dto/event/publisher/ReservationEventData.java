package com.dra.inventory_service.dto.event.publisher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationEventData {

    private Long orderId;
    private Double amount;

}
