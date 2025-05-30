package com.dra.inventory_service.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dra.inventory_service.dto.InventoryData;
import com.dra.inventory_service.dto.InventoryReservationData;
import com.dra.inventory_service.dto.request.CancelReservationData;
import com.dra.inventory_service.dto.request.CreateReservationData;
import com.dra.inventory_service.dto.request.ProductQuantData;
import com.dra.inventory_service.dto.request.ReservationSearchData;
import com.dra.inventory_service.entity.InventoryReservationEntity;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.enums.InventoryReservationStatus;
import com.dra.inventory_service.extension.BadException;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.repository.InventoryReservationRepository;
import com.dra.inventory_service.repository.specification.InventoryReservationSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryReservationService {

    private final InventoryReservationRepository reservationRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public Page<InventoryReservationData> getReservationList(ReservationSearchData reservationSearchData){
        Specification<InventoryReservationEntity> specification = InventoryReservationSpecification.hasOrderId(reservationSearchData.getOrderId())
                                        .and(InventoryReservationSpecification.containsProductCode(reservationSearchData.getProductCode()))
                                        .and(InventoryReservationSpecification.hasStatus(reservationSearchData.getStatus()));

        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(reservationSearchData.getPage(), reservationSearchData.getPageSize(), sort);
        Page<InventoryReservationEntity> pageEntityList = this.reservationRepository.findAll(specification, pageable);
        List<InventoryReservationData> dataList = null;
        Page<InventoryReservationData> dataPageList = new PageImpl<>(dataList, pageable, pageEntityList.getTotalElements());
        return dataPageList;
    }

    @Transactional
    public void createReservation(@Valid CreateReservationData createReservationData){
        ProductQuantData productQuantData = new ProductQuantData(createReservationData.getQuantity());
        InventoryData inventoryData = this.inventoryService.removeProductFromInventory(
                                                createReservationData.getProductCode(),
                                                productQuantData
                                            );
        ProductEntity productEntity = this.productService.getProductEntity(createReservationData.getProductCode());
        InventoryReservationEntity reservationEntity = new InventoryReservationEntity();
        reservationEntity.setOrderId(createReservationData.getOrderId());
        reservationEntity.setPrice(productEntity.getPrice());
        reservationEntity.setProduct(productEntity);
        reservationEntity.setQuantity(createReservationData.getQuantity());
        reservationEntity.setStatus(InventoryReservationStatus.RESERVED);
        this.reservationRepository.save(reservationEntity);
    }

    @Transactional
    public void cancelReservation(@Valid CancelReservationData cancelReservationData){
        InventoryReservationEntity reservationEntity = this.reservationRepository.findByOrderId(cancelReservationData.getOrderId())
                                                                                .orElseThrow(() -> new NotFoundException("Reservation is not found."));

        if(reservationEntity.getStatus().equals(InventoryReservationStatus.FAILED)) throw new BadException("Reservation is already canceled.");

        ProductQuantData productQuantData = new ProductQuantData(reservationEntity.getQuantity());                                                           
        InventoryData inventoryData = this.inventoryService.addProductToInventory(
            reservationEntity.getProduct().getProductCode(), 
            productQuantData
        );
        reservationEntity.setStatus(InventoryReservationStatus.FAILED);
        this.reservationRepository.save(reservationEntity);
    }

}
