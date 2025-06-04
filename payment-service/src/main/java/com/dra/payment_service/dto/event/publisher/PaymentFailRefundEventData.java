package com.dra.payment_service.dto.event.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailRefundEventData {

    private Long orderId;
    private String reason;

}
