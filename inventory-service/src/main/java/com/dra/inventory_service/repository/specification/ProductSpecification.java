package com.dra.inventory_service.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import com.dra.inventory_service.entity.ProductEntity;

public class ProductSpecification {

    public static Specification<ProductEntity> containsName(String name){
        if (name == null || name.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%"+name.toLowerCase()+"%");
    }

    public static Specification<ProductEntity> containsProductCode(String code){
        if (code == null || code.isBlank()) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("product_code")), "%"+code.toLowerCase()+"%");
    }

    public static Specification<ProductEntity> hasStatus(String status){
        if (status == null || status.isBlank()) return null;
        return (root, query, cb) -> cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

} 