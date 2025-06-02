package com.dra.inventory_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.inventory_service.dto.response.InventoryData;
import com.dra.inventory_service.entity.InventoryEntity;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryData entityTodto(InventoryEntity inventoryEntity);

    @Mapping(target = "product.inventoryData", ignore = true)
    @Mapping(target = "product.inventoryReservations", ignore = true)
    InventoryEntity dtoToEntity(InventoryData inventoryData);

    List<InventoryData> entityListTodtoList(List<InventoryEntity> inventoryEntityList);
    List<InventoryEntity> dtoListToEntityList(List<InventoryData> inventoryDataList);

}
