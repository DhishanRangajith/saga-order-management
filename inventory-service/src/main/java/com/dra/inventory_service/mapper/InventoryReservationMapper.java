package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.inventory_service.dto.event.consumer.ProductDataEventData;
import com.dra.inventory_service.dto.request.InventoryReservationCreateData;
import com.dra.inventory_service.dto.response.InventoryReservationData;
import com.dra.inventory_service.entity.InventoryReservationEntity;

@Mapper(componentModel = "spring")
public interface InventoryReservationMapper {

    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "orderId", source = "reservation.orderId")
    InventoryReservationData entityTodto(InventoryReservationEntity entity);

    @Mapping(target = "product.inventoryData", ignore = true)
    @Mapping(target = "product.inventoryReservations", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "reservationId", ignore = true)
    InventoryReservationEntity dtoToEntity(InventoryReservationData dto);

    List<InventoryReservationEntity> entityListTodtoList(List<InventoryReservationData> dtoList);

    InventoryReservationCreateData toInventoryReservationCreateData(ProductDataEventData ProductDataEventData);
    List<InventoryReservationCreateData> toInventoryReservationCreateDataList(List<ProductDataEventData> ProductDataEventData);
    

}
