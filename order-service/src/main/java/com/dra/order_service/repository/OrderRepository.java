package com.dra.order_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dra.order_service.entity.OrderEntity;
import com.dra.order_service.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{

    Optional<OrderEntity> findByIdAndStatusIn(Long id, List<OrderStatus> statues);

}
