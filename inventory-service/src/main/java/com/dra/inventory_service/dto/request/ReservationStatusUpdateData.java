package com.dra.inventory_service.dto.request;

import com.dra.inventory_service.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationStatusUpdateData {

    @NotNull(message = "Status is required")
    private ReservationStatus status;

}
