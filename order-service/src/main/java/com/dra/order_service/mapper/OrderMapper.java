package com.dra.order_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import com.dra.order_service.dto.OrderData;
import com.dra.order_service.dto.ProductData;
import com.dra.order_service.entity.OrderEntity;
import com.dra.order_service.entity.ProductEntity;

@Mapper
public interface OrderMapper {

    OrderEntity orderDataToOrderEntity(OrderData orderData);
    OrderData orderEntityToOrderData(OrderEntity orderEntity);

    ProductEntity productDataToProductEntity(ProductData productData);
    ProductData productEntityToProductData(ProductEntity productEntity);
    
    List<OrderEntity> orderDataListToOrderEntityList(List<OrderData> orderData);
    List<OrderData> orderEntityListToOrderDataList(List<OrderEntity> orderEntity);

}
