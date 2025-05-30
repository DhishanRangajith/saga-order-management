package com.dra.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.dra.inventory_service.entity.InventoryReservationEntity;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, Long>, JpaSpecificationExecutor<InventoryReservationEntity>{
    Optional<InventoryReservationEntity> findByOrderId(Long orderId);
}
