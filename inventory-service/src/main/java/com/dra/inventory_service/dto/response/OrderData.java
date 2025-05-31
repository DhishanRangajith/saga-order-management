package com.dra.inventory_service.dto.response;

import java.util.List;
import com.dra.inventory_service.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {

    private Long orderId;
    private ReservationStatus status;
    private List<ReservationData> reservations;

}


