package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.dra.inventory_service.dto.InventoryReservationData;
import com.dra.inventory_service.entity.InventoryReservationEntity;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    InventoryReservationData entityTodto(InventoryReservationEntity entity);

    @Mapping(target = "product.inventoryData", ignore = true)
    @Mapping(target = "product.inventoryReservations", ignore = true)
    InventoryReservationEntity dtoToEntity(InventoryReservationData dto);

    List<InventoryReservationData> entityListTodtoList(List<InventoryReservationEntity> entityList);
    List<InventoryReservationEntity> dtoListToEntityList(List<InventoryReservationData> dtoList);

}
