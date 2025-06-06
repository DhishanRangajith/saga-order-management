package com.dra.order_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.order_service.dto.event.publisherEvent.OrderCancelEventData;
import com.dra.order_service.dto.event.publisherEvent.OrderCreateEventData;
import com.dra.order_service.dto.response.OrderData;
import com.dra.order_service.entity.OrderEntity;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "products", source = "orderProducts")
    OrderData toOrderData(OrderEntity orderEntity);

    List<OrderData> toOrderDataList(List<OrderEntity> orderEntityList);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "products", source = "orderProducts")
    OrderCreateEventData toOrderCreateEvent(OrderEntity orderEntity);

    @Mapping(target = "orderId", source = "id")
    OrderCancelEventData toOrderCancelEvent(OrderEntity orderEntity);

    List<OrderCreateEventData> toOrderCreateEventList(List<OrderEntity> orderEntity);


}
