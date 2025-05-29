package com.dra.order_service.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.dra.order_service.annotation.CreateOrderRequestDataValidation;
import com.dra.order_service.dto.OrderData;
import com.dra.order_service.entity.OrderEntity;
import com.dra.order_service.enums.OrderStatus;
import com.dra.order_service.mapper.OrderMapper;
import com.dra.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderData> getOrderList(){
        List<OrderEntity> list = this.orderRepository.findAll();
        List<OrderData> dtoList = this.orderMapper.orderEntityListToOrderDataList(list);
        return dtoList;
    }

    public OrderData getOrder(Long id){
        OrderEntity orderEntity = this.orderRepository.findById(id).orElseThrow(
            () -> {throw new RuntimeException("Order is not found.");}
        );
        OrderData orderData = this.orderMapper.orderEntityToOrderData(orderEntity);
        return orderData;
    }

    @Transactional
    @CreateOrderRequestDataValidation
    public OrderData createOrder(OrderData orderData){
        OrderEntity orderEntity = this.orderMapper.orderDataToOrderEntity(orderData);
        orderEntity.setStatus(OrderStatus.CREATED);
        OrderEntity savedEntity = this.orderRepository.save(orderEntity);
        OrderData savedData = this.orderMapper.orderEntityToOrderData(savedEntity);
        return savedData;
    }

    @Transactional
    public boolean cancelOrder(Long id){
        List<OrderStatus> statuses = List.of(
            OrderStatus.CREATED,
            OrderStatus.INVENTORY_RESERVED
        );
        OrderEntity orderEntity = this.orderRepository.findByIdAndStatusIn(id, statuses).orElseThrow(
            () -> {throw new RuntimeException("An order is not found for eligible to cancel.");}
        );
        orderEntity.setStatus(OrderStatus.CANCELLED);
        this.orderRepository.save(orderEntity);
        return true;
    }

}
