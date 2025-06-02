package com.dra.order_service.specification;

import org.springframework.data.jpa.domain.Specification;
import com.dra.order_service.entity.OrderEntity;
import com.dra.order_service.entity.OrderProductEntity;
import jakarta.persistence.criteria.Join;

public class OrderSpecification {

    public static Specification<OrderEntity> hasStatus(String status){
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("status")), "%"+status.toLowerCase()+"%");
    }

    public static Specification<OrderEntity> hasId(Long id){
        if (id == null) return null;
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<OrderEntity> hasProductCode(String productCode){
        if (productCode == null || productCode.isBlank()) return null;
        return (root, query, cb) -> {
            Join<OrderEntity, OrderProductEntity> orderProductJoin = root.join("orderProducts");
            return cb.like(orderProductJoin.get("orderProductId").get("productCode"), "%"+productCode+"%");
        };
    }

}
