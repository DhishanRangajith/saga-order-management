package com.dra.inventory_service.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.dra.inventory_service.entity.InventoryEntity;


public class InventorySpecification {

    public static Specification<InventoryEntity> containsProductName(String name){
        if (name == null || name.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.join("product").get("name")), "%"+name.toLowerCase()+"%");
    }

    public static Specification<InventoryEntity> containsProductCode(String code){
        if (code == null || code.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.join("product").get("product_code")), "%"+code.toLowerCase()+"%");
    }

    public static Specification<InventoryEntity> hasProductStatus(String status){
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.join("product").get("status")), status.toLowerCase());
    }

}
