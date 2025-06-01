package com.dra.payment_service.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import com.dra.payment_service.entity.PaymentEntity;

public class PaymentSpecification {

    public static Specification<PaymentEntity> hasOrder(Long orderId){
        if (orderId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("order_id"), orderId);
    }

    public static Specification<PaymentEntity> hasStatus(String status){
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("status")), status.toLowerCase());
    }
}
