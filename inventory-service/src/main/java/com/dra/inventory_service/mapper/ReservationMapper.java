package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.dra.inventory_service.dto.response.ReservationData;
import com.dra.inventory_service.entity.ReservationEntity;

@Mapper(componentModel = "spring", uses = {InventoryReservationMapper.class})
public interface ReservationMapper {

    ReservationData toDto(ReservationEntity entity);
    List<ReservationData> toDtoList(List<ReservationEntity> entityList);

}
