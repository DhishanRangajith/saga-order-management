package com.dra.inventory_service.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.dra.inventory_service.dto.InventoryData;
import com.dra.inventory_service.dto.request.InventorySearchData;
import com.dra.inventory_service.dto.request.ProductQuantData;
import com.dra.inventory_service.entity.InventoryEntity;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.extension.BadException;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.mapper.InventoryMapper;
import com.dra.inventory_service.repository.InventoryRepository;
import com.dra.inventory_service.repository.ProductRepository;
import com.dra.inventory_service.repository.specification.InventorySpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;

    public Page<InventoryData> getInventotyData(InventorySearchData inventorySearchData){
        Pageable pageable = PageRequest.of(inventorySearchData.getPage(), inventorySearchData.getPageSize());
        Specification<InventoryEntity> specification = Specification.allOf(
                                                            InventorySpecification.containsProductName(inventorySearchData.getProductName()),
                                                            InventorySpecification.containsProductCode(inventorySearchData.getProductCode()),
                                                            InventorySpecification.hasProductStatus(inventorySearchData.getProductStatus())
                                                        );

        Page<InventoryEntity> entityPageList = this.inventoryRepository.findAll(specification, pageable);
        List<InventoryData> dataList = this.inventoryMapper.entityListTodtoList(entityPageList.getContent());
        Page<InventoryData> dataPageList = new PageImpl<>(dataList, pageable, entityPageList.getTotalElements());
        return dataPageList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public InventoryData addProductToInventory(String productCode, ProductQuantData productQuantData){
        Optional<InventoryEntity> entityOption = this.inventoryRepository.findByProduct_ProductCode(productCode);
        InventoryEntity savingEntity;
        if(entityOption.isPresent()){
            savingEntity = entityOption.get();
            Double newQuantity = savingEntity.getQuantity() + productQuantData.getQuantity();
            savingEntity.setQuantity(newQuantity);
        }else{
            ProductEntity productEntity = this.productRepository.findByProductCode(productCode)
                                                                .orElseThrow(() -> new NotFoundException("Product is not found."));
            savingEntity = new InventoryEntity();
            savingEntity.setProduct(productEntity);
            savingEntity.setQuantity(productQuantData.getQuantity());
        }

        InventoryEntity savedEntity = this.inventoryRepository.save(savingEntity);
        InventoryData data = this.inventoryMapper.entityTodto(savedEntity);
        return data;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public InventoryData removeProductFromInventory(String productCode, ProductQuantData productQuantData){
        Optional<InventoryEntity> entityOption = this.inventoryRepository.findByProduct_ProductCode(productCode);
        InventoryEntity savingEntity;
        if(entityOption.isPresent() && entityOption.get().getQuantity() >= productQuantData.getQuantity()){
            savingEntity = entityOption.get();
            Double newQuantity = savingEntity.getQuantity() - productQuantData.getQuantity();
            savingEntity.setQuantity(newQuantity);
        }else{
            throw new BadException("The product has not enought quantiry to remove.");
        }

        InventoryEntity savedEntity = this.inventoryRepository.save(savingEntity);
        InventoryData data = this.inventoryMapper.entityTodto(savedEntity);
        return data;
    }

}
