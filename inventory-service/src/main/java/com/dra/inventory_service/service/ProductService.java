package com.dra.inventory_service.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.dra.inventory_service.dto.ProductData;
import com.dra.inventory_service.dto.request.CreateProductData;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.mapper.ProductMapper;
import com.dra.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public List<ProductData> getProductList(){
        List<ProductEntity> entities = this.productRepository.findAll();
        List<ProductData> dataList = this.productMapper.productEntityListToProductDataList(entities);
        return dataList;
    }

    public ProductData getProduct(String code){
        ProductEntity entity = this.getProductEntity(code);
        ProductData data = this.productMapper.productEntityToProductData(entity);
        return data;
    }

    public ProductEntity getProductEntity(String code){
        ProductEntity entity = this.productRepository.findByProductCode(code).orElseThrow(
            () -> {throw new NotFoundException("Product is not found.");}
        );
        return entity;
    }

    public ProductData createProduct(CreateProductData createProductData){
        ProductEntity entity = this.productMapper.createDataToEntity(createProductData);
        ProductEntity savedEntity = this.productRepository.save(entity);
        ProductData data = this.productMapper.productEntityToProductData(savedEntity);
        return data;
    }

    //Only can update price and status
    public ProductData partialUpdateProduct(Long id, ProductData productData){
        ProductEntity entity = this.productRepository.findById(id).orElseThrow(
            () -> {throw new NotFoundException("Product is not found.");}
        );

        //Update status, price, name if available
        this.productMapper.updateEntityWithUpdatingFields(productData, entity);
        
        ProductEntity savedEntity = this.productRepository.save(entity);
        ProductData savedData = this.productMapper.productEntityToProductData(savedEntity);
        return savedData;
    }

}
