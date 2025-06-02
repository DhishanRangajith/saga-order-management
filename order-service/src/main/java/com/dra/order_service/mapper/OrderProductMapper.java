package com.dra.order_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dra.order_service.dto.response.ProductData;
import com.dra.order_service.entity.OrderProductEntity;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    @Mapping(target = "name", source = "productName")
    @Mapping(target = "productCode", source = "orderProductId.productCode")
    ProductData toProductData(OrderProductEntity orderProductEntity);

    @Mapping(target = "name", source = "productName")
    @Mapping(target = "productCode", source = "orderProductId.productCode")
    List<ProductData> toProductDataList(List<OrderProductEntity> orderProductEntity);

}
