package com.dra.inventory_service.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateData {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Reservations are required")
    @Size(min=1 , message = "Reservations should be grater that 0.")
    private List<ReservationCreateData> reservations;

}
