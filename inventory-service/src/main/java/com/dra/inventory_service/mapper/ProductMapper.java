package com.dra.inventory_service.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.dra.inventory_service.dto.ProductData;
import com.dra.inventory_service.dto.request.CreateProductData;
import com.dra.inventory_service.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductData productEntityToProductData(ProductEntity productEntity);

    @Mapping(target = "inventoryData", ignore = true)
    @Mapping(target = "inventoryReservations", ignore = true)
    ProductEntity productDataToProductEntity(ProductData productData);

    @Mapping(target = "inventoryData", ignore = true)
    @Mapping(target = "inventoryReservations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    ProductEntity createDataToEntity(CreateProductData productData);

    List<ProductData> productEntityListToProductDataList(List<ProductEntity> productEntityList);
    List<ProductEntity> productDataListToProductEntityList(List<ProductData> productDataList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "inventoryData", ignore = true)
    @Mapping(target = "inventoryReservations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    void updateEntityWithUpdatingFields(CreateProductData dto, @MappingTarget ProductEntity entity);

}
