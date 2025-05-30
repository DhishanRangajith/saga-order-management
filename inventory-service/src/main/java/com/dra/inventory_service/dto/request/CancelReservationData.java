package com.dra.inventory_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelReservationData {

    @NotNull(message = "Order is is required.")
    private Long orderId;
}
