package com.dra.inventory_service.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.dra.inventory_service.dto.request.CreateProductData;
import com.dra.inventory_service.dto.request.ProductSearchData;
import com.dra.inventory_service.dto.response.ProductData;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.mapper.ProductMapper;
import com.dra.inventory_service.repository.ProductRepository;
import com.dra.inventory_service.repository.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Page<ProductData> getProductList(ProductSearchData productSearchData){
        Specification<ProductEntity> specification = Specification.allOf(
                ProductSpecification.containsName(productSearchData.getName()),
                ProductSpecification.hasStatus(productSearchData.getStatus()),
                ProductSpecification.containsProductCode(productSearchData.getProductCode())
            );
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(productSearchData.getPage(), productSearchData.getPageSize(), sort);
        Page<ProductEntity> entityPageList = this.productRepository.findAll(specification, pageable);
        List<ProductData> dataList = this.productMapper.productEntityListToProductDataList(entityPageList.getContent());
        Page<ProductData> dataPageList = new PageImpl<>(dataList, pageable, entityPageList.getTotalElements());
        return dataPageList;
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

    //Only can update price, name and status
    public ProductData partialUpdateProduct(String code, CreateProductData productData){
        ProductEntity entity = this.productRepository.findByProductCode(code).orElseThrow(
            () -> {throw new NotFoundException("Product is not found.");}
        );

        //Update status, price, name if available
        this.productMapper.updateEntityWithUpdatingFields(productData, entity);
        
        ProductEntity savedEntity = this.productRepository.save(entity);
        ProductData savedData = this.productMapper.productEntityToProductData(savedEntity);
        return savedData;
    }

}
