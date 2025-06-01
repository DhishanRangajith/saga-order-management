package com.dra.payment_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import com.dra.payment_service.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class PaymentEntity extends CommonEntity{
    
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Override
    @PrePersist
    public void createOn(){
        this.id = UUID.randomUUID();
        super.setCreatedAt(LocalDateTime.now());
        super.setUpdatedAt(LocalDateTime.now());
    }


}
