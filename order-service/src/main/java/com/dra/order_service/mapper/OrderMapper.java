package com.dra.order_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.dra.order_service.dto.response.OrderData;
import com.dra.order_service.entity.OrderEntity;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "products", source = "orderProducts")
    OrderData toOrderData(OrderEntity orderEntity);

    @Mapping(target = "products", source = "orderProducts")
    List<OrderData> toOrderDataList(List<OrderEntity> orderEntityList);

}
