package com.dra.inventory_service.dto.request;

import com.dra.inventory_service.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelReservationData {

    @NotNull(message = "Status is invalid.")
    private ReservationStatus status;
}
