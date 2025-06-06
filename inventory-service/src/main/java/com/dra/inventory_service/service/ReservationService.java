package com.dra.inventory_service.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dra.inventory_service.dto.event.publisher.OrderEventData;
import com.dra.inventory_service.dto.event.publisher.ReservationEventData;
import com.dra.inventory_service.dto.request.ReservationCreateData;
import com.dra.inventory_service.dto.request.ReservationSearchData;
import com.dra.inventory_service.dto.request.ReservationStatusUpdateData;
import com.dra.inventory_service.dto.request.ProductQuantData;
import com.dra.inventory_service.dto.request.InventoryReservationCreateData;
import com.dra.inventory_service.dto.response.ReservationData;
import com.dra.inventory_service.entity.InventoryReservationEntity;
import com.dra.inventory_service.entity.ReservationEntity;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.entity.ReservationId;
import com.dra.inventory_service.enums.ReservationStatus;
import com.dra.inventory_service.extension.BadException;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.mapper.ReservationMapper;
import com.dra.inventory_service.repository.InventoryReservationRepository;
import com.dra.inventory_service.repository.ReservationRepository;
import com.dra.inventory_service.repository.specification.ReservationSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final ReservationMapper reservationMapper;
    private final KafkaEventProcessor kafkaEventProcessor;

    public ReservationEntity getReservationEntityByOrderId(Long orderId){
        return this.reservationRepository.findByOrderId(orderId)
                                .orElseThrow(() -> new NotFoundException("Reservation is not found."));
    }

    public ReservationData getReservationByOrderId(Long orderId){
        ReservationEntity reservationEntity = this.getReservationEntityByOrderId(orderId);
        ReservationData reservationData = this.reservationMapper.toDto(reservationEntity);
        return reservationData;
    }

    public Page<ReservationData> searchReservations(ReservationSearchData reservationSearchData){
        Specification<ReservationEntity> specification = Specification.allOf(
                                    ReservationSpecification.hasOrderId(reservationSearchData.getOrderId()),
                                    ReservationSpecification.containsProductCode(reservationSearchData.getProductCode()),
                                    ReservationSpecification.hasStatus(reservationSearchData.getStatus())
                                );
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(reservationSearchData.getPage(), reservationSearchData.getPageSize(), sort);
        Page<ReservationEntity> pageEntityList = this.reservationRepository.findAll(specification, pageable);
        List<ReservationData> dataList = this.reservationMapper.toDtoList(pageEntityList.getContent());
        Page<ReservationData> dataPageList = new PageImpl<>(dataList, pageable, pageEntityList.getTotalElements());
        return dataPageList;
    }

    @Transactional
    public ReservationData createReservation(ReservationCreateData reservationCreateData){
        boolean isReservationExists = this.reservationRepository.existsByOrderId(reservationCreateData.getOrderId());
        if(isReservationExists)
            this.kafkaEventProcessor.publishOrderCreationFailedEvent(reservationCreateData.getOrderId(), ReservationStatus.INVALID_DATA);

        //create reservation
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setOrderId(reservationCreateData.getOrderId());
        reservationEntity.setStatus(ReservationStatus.RESERVED_AWAITING_PAYMENT);
        ReservationEntity savedReservationEntity = this.reservationRepository.save(reservationEntity);

        List<InventoryReservationEntity> reservations = new ArrayList<>();
        Double total = 0.0;
        //create reservations
        List<InventoryReservationCreateData> irCreateDataList = reservationCreateData.getReservations();
        for(InventoryReservationCreateData irCreateData : irCreateDataList){
            ProductEntity productEntity;
            try{
                productEntity = this.productService.getProductEntity(irCreateData.getProductCode());
            }catch(Exception exp){
                //Publish event
                this.kafkaEventProcessor.publishOrderCreationFailedEvent(reservationCreateData.getOrderId(), ReservationStatus.INVALID_DATA);
                return null;
            }

            //remove product quantity from the inventory
            ProductQuantData productQuantData = new ProductQuantData(irCreateData.getQuantity());
            try{
                this.inventoryService.removeProductFromInventory(
                        productEntity.getProductCode(),
                        productQuantData
                    );
            }catch(BadException badException){
                //Publish event
                this.kafkaEventProcessor.publishOrderCreationFailedEvent(reservationCreateData.getOrderId(), ReservationStatus.NOT_ENOUGHT_INVENTORY);
                return null;
            }

            //craete reservation
            ReservationId reservationId = new ReservationId(savedReservationEntity.getOrderId(), productEntity.getId());
            InventoryReservationEntity irEntity = new InventoryReservationEntity();
            irEntity.setReservationId(reservationId);
            irEntity.setReservation(reservationEntity);
            irEntity.setProduct(productEntity);
            irEntity.setQuantity(irCreateData.getQuantity());
            irEntity.setPrice(productEntity.getPrice());
            InventoryReservationEntity savedirEntity = this.inventoryReservationRepository.save(irEntity);
            total += (productEntity.getPrice() * irCreateData.getQuantity());
            reservations.add(savedirEntity);
        }
        savedReservationEntity.setTotal(total);
        this.reservationRepository.save(savedReservationEntity);
        savedReservationEntity.setReservations(reservations);

        //Publish event
        ReservationEventData reservationEventData = this.reservationMapper.toReservationEventData(savedReservationEntity);
        this.kafkaEventProcessor.publishReservationCreatedEvent(reservationEventData);
        this.kafkaEventProcessor.publishOrderStatusChangedEvent(new OrderEventData(reservationCreateData.getOrderId(), ReservationStatus.RESERVED_AWAITING_PAYMENT.name()));

        ReservationData reservationData = this.reservationMapper.toDto(savedReservationEntity);
        return reservationData;
    }

    @Transactional
    public ReservationEntity releaseReservationItems(Long orderId, ReservationStatus newStatus){

        List<ReservationStatus> eligibleNewStatus = List.of(
            ReservationStatus.RELEASED_ORDER_CANCELLED,
            ReservationStatus.RELEASED_PAYMENT_FAILED
        );

        if(!eligibleNewStatus.contains(newStatus))
            // throw new BadException("Invalid status transition. The requested status '" + newStatus.getLabel() + "' is not allowed.");
            return null;
        
        ReservationEntity reservationEntity = this.getReservationEntityByOrderId(orderId);

        List<ReservationStatus> eligibleStatusesForRevertBack = List.of(
            ReservationStatus.RESERVED_AWAITING_PAYMENT,
            ReservationStatus.RESERVED_PAYMENT_COMPLETED
        );

        if(!eligibleStatusesForRevertBack.contains(reservationEntity.getStatus())) 
            return null;
            //throw new BadException("Restoration failed. " + reservationEntity.getStatus().getLabel().toLowerCase() + ".");
        
        //cancel reservations
        List<InventoryReservationEntity> reservations = reservationEntity.getReservations();
        for(InventoryReservationEntity reservation : reservations){
            ProductQuantData productQuantData = new ProductQuantData(reservation.getQuantity());
            this.inventoryService.addProductToInventory(
                reservation.getProduct().getProductCode(), 
                productQuantData
            );
        }

        //cancel reservation
        reservationEntity.setStatus(newStatus);
        ReservationEntity savedReservationEntity = this.reservationRepository.save(reservationEntity);

        //publish events
        if(newStatus.equals(ReservationStatus.RELEASED_PAYMENT_FAILED)){
            this.kafkaEventProcessor.publishOrderCancellationFailedEvent(new OrderEventData(orderId, ReservationStatus.RELEASED_PAYMENT_FAILED.name()));
        }else if(newStatus.equals(ReservationStatus.RELEASED_ORDER_CANCELLED)){
            this.kafkaEventProcessor.publishOrderCancellationSuccessEvent(new OrderEventData(orderId, ReservationStatus.RELEASED_ORDER_CANCELLED.name()));
        }
        return savedReservationEntity;
    }

    @Transactional
    public ReservationEntity setPaymentSuccessToReservation(Long orderId){
        ReservationEntity reservationEntity = this.getReservationEntityByOrderId(orderId);

        List<ReservationStatus> eligibleStatusesForRevertBack = List.of(
            ReservationStatus.RESERVED_AWAITING_PAYMENT
        );

        if(!eligibleStatusesForRevertBack.contains(reservationEntity.getStatus())) 
            return null;
            //throw new BadException("Restoration status update failed. " + reservationEntity.getStatus().getLabel().toLowerCase() + ".");

        reservationEntity.setStatus(ReservationStatus.RESERVED_PAYMENT_COMPLETED);
        ReservationEntity savedReservationEntity = this.reservationRepository.save(reservationEntity);
        //publish event
        this.kafkaEventProcessor.publishOrderCreationSuccessEvent(new OrderEventData(orderId, ReservationStatus.RESERVED_PAYMENT_COMPLETED.name()));
        return savedReservationEntity;
    }

    @Transactional
    public boolean updateReservationStatus(Long orderId, ReservationStatusUpdateData reservationStatusUpdateData){

        List<ReservationStatus> eligibleUpdateStatuses = List.of(ReservationStatus.FULFILLED_DELIVERED);

        if(!eligibleUpdateStatuses.contains(reservationStatusUpdateData.getStatus())) 
            throw new BadException("Update status is not eligible.");

        ReservationEntity reservationEntity = this.getReservationEntityByOrderId(orderId);

        List<ReservationStatus> eligibleEntityStatuses = List.of(ReservationStatus.RESERVED_PAYMENT_COMPLETED);

        if(!eligibleEntityStatuses.contains(reservationEntity.getStatus())) 
            throw new BadException("Existing status is not eligible to update with new status.");

        reservationEntity.setStatus(reservationStatusUpdateData.getStatus());
        this.reservationRepository.save(reservationEntity);

        //publis event
        this.kafkaEventProcessor.publishOrderStatusChangedEvent(new OrderEventData(orderId, ReservationStatus.FULFILLED_DELIVERED.name()));

        return true;
    }

}
