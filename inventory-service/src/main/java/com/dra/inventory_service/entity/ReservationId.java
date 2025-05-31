package com.dra.inventory_service.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationId implements Serializable{

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

}
