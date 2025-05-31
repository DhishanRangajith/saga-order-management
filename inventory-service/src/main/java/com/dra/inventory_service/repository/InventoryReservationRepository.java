package com.dra.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.dra.inventory_service.entity.InventoryReservationEntity;
import com.dra.inventory_service.entity.ReservationId;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, ReservationId>, JpaSpecificationExecutor<InventoryReservationEntity>{
}
