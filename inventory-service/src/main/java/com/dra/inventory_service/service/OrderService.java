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
import com.dra.inventory_service.dto.request.CancelReservationData;
import com.dra.inventory_service.dto.request.OrderCreateData;
import com.dra.inventory_service.dto.request.OrderSearchData;
import com.dra.inventory_service.dto.request.ProductQuantData;
import com.dra.inventory_service.dto.request.ReservationCreateData;
import com.dra.inventory_service.dto.response.OrderData;
import com.dra.inventory_service.entity.InventoryReservationEntity;
import com.dra.inventory_service.entity.OrderEntity;
import com.dra.inventory_service.entity.ProductEntity;
import com.dra.inventory_service.entity.ReservationId;
import com.dra.inventory_service.enums.ReservationStatus;
import com.dra.inventory_service.extension.BadException;
import com.dra.inventory_service.extension.NotFoundException;
import com.dra.inventory_service.mapper.OrderMapper;
import com.dra.inventory_service.repository.InventoryReservationRepository;
import com.dra.inventory_service.repository.OrderRepository;
import com.dra.inventory_service.repository.specification.OrderSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryReservationRepository reservationRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final OrderMapper orderMapper;

    public OrderData getOrder(Long orderId){
        OrderEntity orderEntity = this.orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order is not found."));
        OrderData orderData = this.orderMapper.toDto(orderEntity);
        return orderData;
    }

    public Page<OrderData> getOrdersList(OrderSearchData orderSearchData){
        Specification<OrderEntity> specification = Specification.allOf(
                                    OrderSpecification.hasOrderId(orderSearchData.getOrderId()),
                                    OrderSpecification.containsProductCode(orderSearchData.getProductCode()),
                                    OrderSpecification.hasStatus(orderSearchData.getStatus())
                                );
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(orderSearchData.getPage(), orderSearchData.getPageSize(), sort);
        Page<OrderEntity> pageEntityList = this.orderRepository.findAll(specification, pageable);
        List<OrderData> dataList = this.orderMapper.toDtoList(pageEntityList.getContent());
        Page<OrderData> dataPageList = new PageImpl<>(dataList, pageable, pageEntityList.getTotalElements());
        return dataPageList;
    }

    @Transactional
    public OrderData createOrder(OrderCreateData orderCreateData){
        boolean isOrderExists = this.orderRepository.existsByOrderId(orderCreateData.getOrderId());
        if(isOrderExists) throw new BadException("Order is already exists.");

        //create order
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderCreateData.getOrderId());
        orderEntity.setStatus(ReservationStatus.RESERVED);
        OrderEntity savedOrderEntity = this.orderRepository.save(orderEntity);
        savedOrderEntity.setReservations(new ArrayList<>());

        //create reservations
        List<ReservationCreateData> reservationCreateDataList = orderCreateData.getReservations();
        for(ReservationCreateData reservationCreateData : reservationCreateDataList){

            ProductEntity productEntity = this.productService.getProductEntity(reservationCreateData.getProductCode());

            //remove product quantity from the inventory
            ProductQuantData productQuantData = new ProductQuantData(reservationCreateData.getQuantity());
            this.inventoryService.removeProductFromInventory(
                    productEntity.getProductCode(),
                    productQuantData
                );

            //craete reservation
            ReservationId reservationId = new ReservationId(savedOrderEntity.getOrderId(), productEntity.getId());
            InventoryReservationEntity reservationEntity = new InventoryReservationEntity();
            reservationEntity.setReservationId(reservationId);
            reservationEntity.setOrder(orderEntity);
            reservationEntity.setProduct(productEntity);
            reservationEntity.setQuantity(reservationCreateData.getQuantity());
            reservationEntity.setPrice(productEntity.getPrice());
            InventoryReservationEntity savedReservationEntity = this.reservationRepository.save(reservationEntity);
            savedOrderEntity.getReservations().add(savedReservationEntity);
        }

        OrderData orderData = this.orderMapper.toDto(savedOrderEntity);
        return orderData;
    }

    public OrderData cancelOrder(@Valid CancelReservationData cancelReservationData){
        OrderEntity orderEntity = this.orderRepository.findByOrderId(cancelReservationData.getOrderId())
                                                        .orElseThrow(() -> new NotFoundException("Order is not found."));

        if(!orderEntity.getStatus().equals(ReservationStatus.RESERVED)) 
                throw new BadException("The order is not eligible to cancel. Already "+orderEntity.getStatus()+".");
        
        //cancel reservations
        List<InventoryReservationEntity> reservations = orderEntity.getReservations();
        for(InventoryReservationEntity reservation : reservations){
            ProductQuantData productQuantData = new ProductQuantData(reservation.getQuantity());
            this.inventoryService.addProductToInventory(
                reservation.getProduct().getProductCode(), 
                productQuantData
            );
        }

        //cancel order
        orderEntity.setStatus(ReservationStatus.CANCELLED);
        OrderEntity savedOrderEntity = this.orderRepository.save(orderEntity);

        OrderData orderData = this.orderMapper.toDto(savedOrderEntity);
        return orderData;
    }

}
