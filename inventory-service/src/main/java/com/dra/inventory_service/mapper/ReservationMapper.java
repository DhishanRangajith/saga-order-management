package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.inventory_service.dto.event.consumer.OrderCreateEventData;
import com.dra.inventory_service.dto.event.publisher.ReservationEventData;
import com.dra.inventory_service.dto.request.ReservationCreateData;
import com.dra.inventory_service.dto.response.ReservationData;
import com.dra.inventory_service.entity.ReservationEntity;

@Mapper(componentModel = "spring", uses = {InventoryReservationMapper.class})
public interface ReservationMapper {

    ReservationData toDto(ReservationEntity entity);
    List<ReservationData> toDtoList(List<ReservationEntity> entityList);
    
    @Mapping(target = "amount", source = "total")
    ReservationEventData toReservationEventData(ReservationEntity erReservationEntity);

    @Mapping(target = "reservations", source = "products")
    ReservationCreateData toReservationCreateDataFromEvent(OrderCreateEventData orderCreateEventData);

}
