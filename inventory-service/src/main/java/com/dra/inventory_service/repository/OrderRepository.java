package com.dra.inventory_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.dra.inventory_service.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity>{

    Optional<OrderEntity> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}
