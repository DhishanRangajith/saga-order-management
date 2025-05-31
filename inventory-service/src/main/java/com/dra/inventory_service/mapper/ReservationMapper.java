package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.dra.inventory_service.dto.InventoryReservationData;
import com.dra.inventory_service.dto.response.ReservationData;
import com.dra.inventory_service.entity.InventoryReservationEntity;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "orderId", source = "order.orderId")
    InventoryReservationData entityTodto(InventoryReservationEntity entity);

    @Mapping(target = "product.inventoryData", ignore = true)
    @Mapping(target = "product.inventoryReservations", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "reservationId", ignore = true)
    InventoryReservationEntity dtoToEntity(InventoryReservationData dto);

    List<InventoryReservationData> entityListTodtoList(List<InventoryReservationEntity> entityList);
    List<InventoryReservationEntity> dtoListToEntityList(List<InventoryReservationData> dtoList);

    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "productName", source = "product.name")
    ReservationData entityToReservationData(InventoryReservationEntity entity);

}
