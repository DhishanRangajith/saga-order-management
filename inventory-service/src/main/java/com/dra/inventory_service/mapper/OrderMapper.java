package com.dra.inventory_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import com.dra.inventory_service.dto.response.OrderData;
import com.dra.inventory_service.entity.OrderEntity;

@Mapper(componentModel = "spring", uses = {ReservationMapper.class})
public interface OrderMapper {

    OrderData toDto(OrderEntity entity);
    List<OrderData> toDtoList(List<OrderEntity> entityList);

}
