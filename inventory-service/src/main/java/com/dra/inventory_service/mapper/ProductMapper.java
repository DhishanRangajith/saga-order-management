package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.dra.inventory_service.dto.ProductData;
import com.dra.inventory_service.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductData productEntityToProductData(ProductEntity productEntity);

    @Mapping(target = "inventoryData", ignore = true)
    @Mapping(target = "inventoryReservations", ignore = true)
    ProductEntity productDataToProductEntity(ProductData productData);

    List<ProductData> productEntityListToProductDataList(List<ProductEntity> productEntityList);
    List<ProductEntity> productDataListToProductEntityList(List<ProductData> productDataList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "inventoryData", ignore = true)
    @Mapping(target = "inventoryReservations", ignore = true)
    void updateEntityWithUpdatingFields(ProductData dto, @MappingTarget ProductEntity entity);

}
