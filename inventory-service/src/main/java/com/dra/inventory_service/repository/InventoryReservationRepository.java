package com.dra.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dra.inventory_service.entity.InventoryReservationEntity;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, Long>{

}
