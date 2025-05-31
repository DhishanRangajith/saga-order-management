package com.dra.inventory_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.dra.inventory_service.entity.InventoryReservationEntity;
import com.dra.inventory_service.entity.ReservationId;
import com.dra.inventory_service.enums.ReservationStatus;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, ReservationId>, JpaSpecificationExecutor<InventoryReservationEntity>{
    // List<InventoryReservationEntity> findAllByOrderId(Long orderId);
    // boolean existsByOrderIdAndStatus(Long orderId, ReservationStatus status);
    // boolean existsByOrderId(Long orderId);
}
