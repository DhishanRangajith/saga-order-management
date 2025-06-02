package com.dra.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dra.order_service.entity.OrderProductEntity;
import com.dra.order_service.entity.OrderProductId;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, OrderProductId>{


}
