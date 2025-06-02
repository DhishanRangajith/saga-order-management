package com.dra.inventory_service.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import com.dra.inventory_service.entity.ReservationEntity;

public class ReservationSpecification {

    public static Specification<ReservationEntity> hasOrderId(Long orderId){
        if (orderId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("order_id"), orderId);
    }

    public static Specification<ReservationEntity> containsProductCode(String productCode){
        if (productCode == null || productCode.isBlank()) return null;
        return (root, query, cb) -> cb.like(root.join("reservations").join("product").get("product_code"), "%"+productCode+"%");
    }

    public static Specification<ReservationEntity> hasStatus(String status){
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

}
