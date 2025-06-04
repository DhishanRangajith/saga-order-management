package com.dra.order_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.order_service.dto.publisherEvent.OrderCancelEvent;
import com.dra.order_service.dto.publisherEvent.OrderCreateEvent;
import com.dra.order_service.dto.response.OrderData;
import com.dra.order_service.entity.OrderEntity;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "products", source = "orderProducts")
    OrderData toOrderData(OrderEntity orderEntity);

    List<OrderData> toOrderDataList(List<OrderEntity> orderEntityList);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "products", source = "orderProducts")
    OrderCreateEvent toOrderCreateEvent(OrderEntity orderEntity);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "reason", ignore = true)
    OrderCancelEvent tOrderCancelEvent(OrderEntity orderEntity);

    List<OrderCreateEvent> toOrderCreateEventList(List<OrderEntity> orderEntity);


}
